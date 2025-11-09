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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Wrapper minimale per le chiamate HTTP verso il backend Spring Boot.
 */
public class BackendGateway {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);
    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String baseUrl;

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
        return "http://localhost:8080";
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
            return List.of();
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
            return List.of();
        }
    }

    public List<MonthlyRevenueSnapshot> fetchMonthlyRevenue() {
        try {
            HttpResponse<String> response = send("/api/fatture/andamento");
            if (response == null || response.statusCode() >= 400 || response.body() == null
                    || response.body().isBlank()) {
                return List.of();
            }
            return mapper.readValue(response.body(), new TypeReference<List<MonthlyRevenueSnapshot>>() {
            });
        } catch (IOException | InterruptedException e) {
            return List.of();
        }
    }

    private HttpResponse<String> send(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(resolve(path))
                .timeout(TIMEOUT)
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private JsonNode readJson(String path) throws IOException, InterruptedException {
        HttpResponse<String> response = send(path);
        if (response == null || response.statusCode() >= 400 || response.body() == null
                || response.body().isBlank()) {
            return null;
        }
        return mapper.readTree(response.body());
    }

    private URI resolve(String path) {
        String normalized = path.startsWith("/") ? path : "/" + path;
        return URI.create(baseUrl + normalized);
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
