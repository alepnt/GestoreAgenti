package com.example.GestoreAgenti.fx.data.backend;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Wrapper minimale per le chiamate HTTP verso il backend Spring Boot.
 */
public class BackendGateway {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private volatile String authToken;

    public BackendGateway() {
        this(resolveBaseUrl());
    }

    public BackendGateway(String baseUrl) {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(TIMEOUT)
                .build();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.baseUrl = Objects.requireNonNull(baseUrl);
    }

    private static String resolveBaseUrl() {
        String fromProperty = System.getProperty("gestoreagenti.backendUrl");
        if (fromProperty != null && !fromProperty.isBlank()) {
            return fromProperty;
        }
        String fromEnv = System.getenv("GESTOREAGENTI_BACKEND_URL");
        if (fromEnv != null && !fromEnv.isBlank()) {
            return fromEnv;
        }
        return "http://localhost:8081";
    }

    public Optional<String> authenticate(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            return Optional.empty();
        }
        clearAuthentication();
        try {
            ObjectNode payload = mapper.createObjectNode();
            payload.put("username", username);
            payload.put("password", password);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(resolve("/api/auth/login"))
                    .timeout(TIMEOUT)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(payload)))
                    .build();
            HttpResponse<String> response = send(request);
            if (isSuccessful(response) && response.body() != null && !response.body().isBlank()) {
                String token = response.body().trim();
                if (token.startsWith("\"") && token.endsWith("\"")) {
                    token = token.substring(1, token.length() - 1);
                }
                setAuthToken(token);
                return Optional.of(token);
            }
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
        }
        clearAuthentication();
        return Optional.empty();
    }

    public void clearAuthentication() {
        setAuthToken(null);
    }

    public boolean isAuthenticated() {
        String token = authToken;
        return token != null && !token.isBlank();
    }

    public List<EmployeeSnapshot> fetchEmployees() {
        try {
            JsonNode root = readJson("/api/dipendenti");
            if (root == null || !root.isArray()) {
                return List.of();
            }
            List<EmployeeSnapshot> result = new ArrayList<>();
            for (JsonNode node : root) {
                Long id = node.path("id").isNumber() ? node.path("id").longValue() : null;
                String nome = text(node, "nome");
                String cognome = text(node, "cognome");
                String email = text(node, "email");
                String team = text(node, "team");
                String ruolo = text(node, "ranking");
                String username = text(node, "username");
                String rawPassword = text(node, "password");
                result.add(new EmployeeSnapshot(id, nome, cognome, ruolo, team, email,
                        normalizePassword(rawPassword), username));
            }
            return result;
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
            return List.of();
        }
    }

    public Optional<EmployeeSnapshot> createEmployee(String firstName, String lastName, String role,
            String team, String email, String username, String password, String userRoleCode) {
        if (!isAuthenticated()) {
            return Optional.empty();
        }
        try {
            ObjectNode payload = mapper.createObjectNode();
            payload.put("nome", firstName);
            payload.put("cognome", lastName);
            if (email != null) {
                payload.put("email", email);
            }
            if (role != null) {
                payload.put("ranking", role);
            }
            if (team != null) {
                payload.put("team", team);
            }
            payload.put("username", username);
            payload.put("password", password);

            HttpResponse<String> response = sendJson("/api/dipendenti", "POST",
                    mapper.writeValueAsString(payload));
            if (!isSuccessful(response)) {
                return Optional.empty();
            }
            JsonNode root = parseBody(response.body());
            Long backendId = root != null && root.path("id").isNumber() ? root.path("id").longValue() : null;
            if (backendId == null) {
                return Optional.empty();
            }

            if (!createUserAccount(backendId, username, password, userRoleCode)) {
                deleteDipendente(backendId);
                return Optional.empty();
            }

            String resolvedFirstName = text(root, "nome");
            String resolvedLastName = text(root, "cognome");
            String resolvedRole = text(root, "ranking");
            String resolvedTeam = text(root, "team");
            String resolvedEmail = text(root, "email");
            String resolvedUsername = text(root, "username");

            return Optional.of(new EmployeeSnapshot(backendId, resolvedFirstName, resolvedLastName,
                    resolvedRole, resolvedTeam, resolvedEmail, password, resolvedUsername));
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
            return Optional.empty();
        }
    }

    public List<InvoiceSnapshot> fetchFattureVendita() {
        return fetchInvoices("/api/fatture/vendita");
    }

    public List<InvoiceSnapshot> fetchFattureRegistrate() {
        return fetchInvoices("/api/fatture/registrate");
    }

    private List<InvoiceSnapshot> fetchInvoices(String path) {
        try {
            JsonNode root = readJson(path);
            if (root == null || !root.isArray()) {
                return List.of();
            }
            List<InvoiceSnapshot> invoices = new ArrayList<>();
            for (JsonNode node : root) {
                String numero = text(node, "numeroFattura");
                LocalDate data = node.path("dataEmissione").isNull() ? null
                        : LocalDate.parse(node.path("dataEmissione").asText());
                String cliente = extractCliente(node.path("cliente"));
                BigDecimal totale = node.path("totale").isNumber()
                        ? node.path("totale").decimalValue()
                        : parseDecimal(node.path("totale"));
                String stato = text(node, "stato");
                boolean registrata = node.path("registrata").asBoolean(false);
                invoices.add(new InvoiceSnapshot(numero, data, cliente, stato, totale, registrata));
            }
            return invoices;
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
            return List.of();
        }
    }

    public List<PaymentSnapshot> fetchPagamenti() {
        try {
            JsonNode root = readJson("/api/pagamenti");
            if (root == null || !root.isArray()) {
                return List.of();
            }
            List<PaymentSnapshot> payments = new ArrayList<>();
            for (JsonNode node : root) {
                String metodo = text(node, "metodo");
                BigDecimal importo = node.path("importo").isNumber()
                        ? node.path("importo").decimalValue()
                        : parseDecimal(node.path("importo"));
                LocalDate dataPagamento = node.path("dataPagamento").isNull() ? null
                        : LocalDate.parse(node.path("dataPagamento").asText());
                String numeroFattura = node.path("fattura").isObject()
                        ? text(node.path("fattura"), "numeroFattura")
                        : text(node, "numeroFattura");
                payments.add(new PaymentSnapshot(numeroFattura, importo, dataPagamento, metodo));
            }
            return payments;
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
            return List.of();
        }
    }

    public List<NotificationSnapshot> fetchNotifications(Long recipientId) {
        if (recipientId == null) {
            return List.of();
        }
        try {
            JsonNode root = readJson("/api/notifications?recipientId=" + recipientId);
            if (root == null || !root.isArray()) {
                return List.of();
            }
            List<NotificationSnapshot> notifications = new ArrayList<>();
            for (JsonNode node : root) {
                String title = text(node, "title");
                String message = text(node, "message");
                LocalDateTime createdAt = node.path("createdAt").isNull() ? null
                        : LocalDateTime.parse(node.path("createdAt").asText());
                boolean read = node.path("read").asBoolean(false);
                notifications.add(new NotificationSnapshot(title, message, createdAt, read));
            }
            return notifications;
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
            return List.of();
        }
    }

    public List<AgendaSnapshot> fetchAgenda(Long dipendenteId) {
        if (dipendenteId == null) {
            return List.of();
        }
        try {
            JsonNode root = readJson("/api/agenda/dipendenti/" + dipendenteId);
            if (root == null || !root.isArray()) {
                return List.of();
            }
            List<AgendaSnapshot> events = new ArrayList<>();
            for (JsonNode node : root) {
                LocalDateTime start = node.path("start").isNull() ? null
                        : LocalDateTime.parse(node.path("start").asText());
                LocalDateTime end = node.path("end").isNull() ? null
                        : LocalDateTime.parse(node.path("end").asText());
                String title = text(node, "title");
                String description = text(node, "description");
                events.add(new AgendaSnapshot(start, end, title, description));
            }
            return events;
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
            return List.of();
        }
    }

    public List<MonthlyRevenueSnapshot> fetchMonthlyRevenue() {
        try {
            HttpResponse<String> response = sendGet("/api/fatture/andamento");
            if (response == null || response.statusCode() >= 400 || response.body() == null
                    || response.body().isBlank()) {
                return List.of();
            }
            return mapper.readValue(response.body(), new TypeReference<List<MonthlyRevenueSnapshot>>() {
            });
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
            return List.of();
        }
    }

    private HttpResponse<String> sendGet(String path) throws IOException, InterruptedException {
        HttpRequest request = request(path)
                .GET()
                .build();
        return send(request);
    }

    private JsonNode readJson(String path) throws IOException, InterruptedException {
        HttpResponse<String> response = sendGet(path);
        if (response == null || response.statusCode() >= 400 || response.body() == null
                || response.body().isBlank()) {
            return null;
        }
        return mapper.readTree(response.body());
    }

    private HttpRequest.Builder request(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(resolve(normalized))
                .timeout(TIMEOUT);
        applyAuthorization(builder);
        return builder;
    }

    private HttpResponse<String> sendJson(String path, String method, String payload)
            throws IOException, InterruptedException {
        HttpRequest request = request(path)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method(method, HttpRequest.BodyPublishers.ofString(payload))
                .build();
        return send(request);
    }

    private HttpResponse<String> sendDelete(String path) throws IOException, InterruptedException {
        HttpRequest request = request(path)
                .DELETE()
                .build();
        return send(request);
    }

    private HttpResponse<String> send(HttpRequest request) throws IOException, InterruptedException {
        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw ex;
        }
    }

    private void applyAuthorization(HttpRequest.Builder builder) {
        String token = authToken;
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }
    }

    private void setAuthToken(String token) {
        this.authToken = (token != null && !token.isBlank()) ? token : null;
    }

    private void handleInterrupted(Exception error) {
        if (error instanceof InterruptedException) {
            Thread.currentThread().interrupt();
        }
    }

    private JsonNode parseBody(String body) throws IOException {
        if (body == null || body.isBlank()) {
            return null;
        }
        return mapper.readTree(body);
    }

    private boolean createUserAccount(Long dipendenteId, String username, String password, String userRoleCode)
            throws IOException, InterruptedException {
        ObjectNode payload = mapper.createObjectNode();
        payload.put("username", username);
        payload.put("passwordHash", password);
        payload.put("ruolo", userRoleCode);
        ObjectNode dipendenteNode = mapper.createObjectNode();
        dipendenteNode.put("id", dipendenteId);
        payload.set("dipendente", dipendenteNode);

        HttpResponse<String> response = sendJson("/api/utenti", "POST", mapper.writeValueAsString(payload));
        return isSuccessful(response);
    }

    private void deleteDipendente(Long dipendenteId) {
        try {
            sendDelete("/api/dipendenti/" + dipendenteId);
        } catch (IOException | InterruptedException e) {
            handleInterrupted(e);
        }
    }

    private boolean isSuccessful(HttpResponse<?> response) {
        if (response == null) {
            return false;
        }
        int status = response.statusCode();
        return status >= 200 && status < 300;
    }

    private URI resolve(String path) {
        return URI.create(baseUrl + path);
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.path(field);
        if (value.isMissingNode() || value.isNull()) {
            return null;
        }
        String raw = value.asText();
        return raw != null && raw.isBlank() ? null : raw;
    }

    private static BigDecimal parseDecimal(JsonNode node) {
        if (node == null || node.isNull()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(node.asText());
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private static String extractCliente(JsonNode clienteNode) {
        if (clienteNode == null || clienteNode.isMissingNode() || clienteNode.isNull()) {
            return null;
        }
        String ragioneSociale = text(clienteNode, "ragioneSociale");
        if (ragioneSociale != null && !ragioneSociale.isBlank()) {
            return ragioneSociale;
        }
        String nome = text(clienteNode, "nome");
        String cognome = text(clienteNode, "cognome");
        if (nome == null && cognome == null) {
            return null;
        }
        if (nome == null) {
            return cognome;
        }
        if (cognome == null) {
            return nome;
        }
        return nome + " " + cognome;
    }

    private static String normalizePassword(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        String trimmed = rawPassword.trim();
        if (trimmed.toLowerCase(Locale.ROOT).startsWith("{noop}")) {
            return trimmed.substring("{noop}".length());
        }
        return trimmed;
    }

    public record EmployeeSnapshot(Long id, String firstName, String lastName, String role,
            String team, String email, String password, String username) {
    }

    public record InvoiceSnapshot(String number, LocalDate issueDate, String customer,
            String state, BigDecimal total, boolean registered) {
    }

    public record PaymentSnapshot(String invoiceNumber, BigDecimal amount,
            LocalDate paymentDate, String method) {
    }

    public record NotificationSnapshot(String title, String message,
            LocalDateTime createdAt, boolean read) {
    }

    public record AgendaSnapshot(LocalDateTime start, LocalDateTime end,
            String title, String description) {
    }

    public record MonthlyRevenueSnapshot(Integer year, Integer month, BigDecimal total) {
    }
}
