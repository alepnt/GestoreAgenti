package com.example.GestoreAgenti.fx.data.backend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BackendGatewayTest {

    private HttpServer server;
    private BackendGateway gateway;

    @BeforeEach
    void setUp() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.start();
        gateway = new BackendGateway("http://localhost:" + server.getAddress().getPort());
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
        if (gateway != null) {
            gateway.clearAuthentication();
        }
    }

    @Test
    void shouldAuthenticateAndPropagateAuthorizationHeader() {
        AtomicReference<String> capturedBody = new AtomicReference<>();
        server.createContext("/api/auth/login", exchange -> {
            capturedBody.set(readRequestBody(exchange));
            sendJson(exchange, 200, "\"jwt-token\"");
        });

        AtomicReference<String> authorizationHeader = new AtomicReference<>();
        server.createContext("/api/dipendenti", exchange -> {
            authorizationHeader.set(exchange.getRequestHeaders().getFirst("Authorization"));
            String body = "[" +
                    "{\"id\":1,\"nome\":\"Mario\",\"cognome\":\"Rossi\",\"email\":\"\",\"team\":null,"
                    + "\"ranking\":\"Manager\",\"username\":\"mrossi\",\"password\":\"{noop} segreto \"}," +
                    "{\"id\":2,\"nome\":\"Laura\",\"cognome\":null,\"email\":\"laura@example.com\"," +
                    "\"team\":\"Vendite\",\"ranking\":null,\"username\":\"laura\",\"password\":null}" +
                    "]";
            sendJson(exchange, 200, body);
        });

        Optional<String> token = gateway.authenticate("alice", "wonderland");

        assertTrue(token.isPresent());
        assertEquals("jwt-token", token.orElseThrow());
        assertTrue(gateway.isAuthenticated());
        assertTrue(capturedBody.get().contains("\"username\":\"alice\""));
        assertTrue(capturedBody.get().contains("\"password\":\"wonderland\""));

        List<BackendGateway.EmployeeSnapshot> employees = gateway.fetchEmployees();
        assertEquals("Bearer jwt-token", authorizationHeader.get());
        assertEquals(2, employees.size());

        BackendGateway.EmployeeSnapshot first = employees.get(0);
        assertEquals(1L, first.id());
        assertEquals("Mario", first.firstName());
        assertEquals("Rossi", first.lastName());
        assertEquals("Manager", first.role());
        assertNull(first.team());
        assertNull(first.email());
        assertEquals("segreto", first.password());
        assertEquals("mrossi", first.username());

        BackendGateway.EmployeeSnapshot second = employees.get(1);
        assertEquals(2L, second.id());
        assertEquals("Laura", second.firstName());
        assertNull(second.lastName());
        assertNull(second.role());
        assertEquals("Vendite", second.team());
        assertEquals("laura@example.com", second.email());
        assertNull(second.password());
        assertEquals("laura", second.username());
    }

    @Test
    void shouldClearAuthenticationAfterFailedLogin() {
        AtomicInteger invocation = new AtomicInteger();
        server.createContext("/api/auth/login", exchange -> {
            readRequestBody(exchange);
            if (invocation.getAndIncrement() == 0) {
                sendJson(exchange, 200, "\"first-token\"");
            } else {
                sendJson(exchange, 500, "error");
            }
        });

        assertTrue(gateway.authenticate("user", "pass").isPresent());
        assertTrue(gateway.isAuthenticated());

        Optional<String> secondAttempt = gateway.authenticate("user", "pass");
        assertTrue(secondAttempt.isEmpty());
        assertFalse(gateway.isAuthenticated());
    }

    @Test
    void shouldCreateEmployeeAndReturnSnapshot() {
        authenticateWithFixedToken();

        AtomicReference<String> createPayload = new AtomicReference<>();
        server.createContext("/api/dipendenti", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                createPayload.set(readRequestBody(exchange));
                String body = "{\"id\":42,\"nome\":\"Mario\",\"cognome\":\"Rossi\",\"ranking\":\"Manager\"," +
                        "\"team\":\"Vendite\",\"email\":\"mario.rossi@example.com\",\"username\":\"mrossi\"}";
                sendJson(exchange, 200, body);
            } else {
                sendJson(exchange, 405, "");
            }
        });

        AtomicReference<String> userPayload = new AtomicReference<>();
        server.createContext("/api/utenti", exchange -> {
            userPayload.set(readRequestBody(exchange));
            sendJson(exchange, 201, "{}");
        });

        Optional<BackendGateway.EmployeeSnapshot> result = gateway.createEmployee(
                "Mario", "Rossi", "Manager", "Vendite", "mario.rossi@example.com", "mrossi", "pwd123", "ROLE_ADMIN");

        assertTrue(result.isPresent());
        BackendGateway.EmployeeSnapshot snapshot = result.orElseThrow();
        assertEquals(42L, snapshot.id());
        assertEquals("Mario", snapshot.firstName());
        assertEquals("Rossi", snapshot.lastName());
        assertEquals("Manager", snapshot.role());
        assertEquals("Vendite", snapshot.team());
        assertEquals("mario.rossi@example.com", snapshot.email());
        assertEquals("pwd123", snapshot.password());
        assertEquals("mrossi", snapshot.username());

        String payload = createPayload.get();
        assertTrue(payload.contains("\"nome\":\"Mario\""));
        assertTrue(payload.contains("\"cognome\":\"Rossi\""));
        assertTrue(payload.contains("\"username\":\"mrossi\""));
        assertTrue(payload.contains("\"password\":\"pwd123\""));

        String userRequest = userPayload.get();
        assertTrue(userRequest.contains("\"username\":\"mrossi\""));
        assertTrue(userRequest.contains("\"passwordHash\":\"pwd123\""));
        assertTrue(userRequest.contains("\"ruolo\":\"ROLE_ADMIN\""));
        assertTrue(userRequest.contains("\"dipendente\""));
    }

    @Test
    void shouldRollbackEmployeeCreationWhenUserCreationFails() {
        authenticateWithFixedToken();

        AtomicReference<String> deletePath = new AtomicReference<>();
        server.createContext("/api/dipendenti", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                readRequestBody(exchange);
                sendJson(exchange, 200, "{\"id\":99,\"nome\":\"Anna\",\"cognome\":\"Bianchi\"}");
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                deletePath.set(exchange.getRequestURI().getPath());
                sendJson(exchange, 204, "");
            } else {
                sendJson(exchange, 405, "");
            }
        });

        server.createContext("/api/utenti", exchange -> {
            readRequestBody(exchange);
            sendJson(exchange, 500, "failure");
        });

        Optional<BackendGateway.EmployeeSnapshot> result = gateway.createEmployee(
                "Anna", "Bianchi", "", null, null, "abianchi", "pwd", "ROLE_USER");

        assertTrue(result.isEmpty());
        assertEquals("/api/dipendenti/99", deletePath.get());
    }

    @Test
    void shouldFetchInvoicesPaymentsNotificationsAgendaAndRevenue() {
        server.createContext("/api/fatture/vendita", exchange -> {
            String body = "[" +
                    "{\"numeroFattura\":\"F-1\",\"dataEmissione\":\"2024-01-10\","
                    + "\"cliente\":{\"ragioneSociale\":\"ACME\"},\"totale\":123.45,\"stato\":\"EMESSA\",\"registrata\":false}," +
                    "{\"numeroFattura\":\"F-2\",\"dataEmissione\":null,\"cliente\":{\"nome\":\"Mario\",\"cognome\":\"Rossi\"},"
                    + "\"totale\":\"not-a-number\",\"stato\":null,\"registrata\":true}" +
                    "]";
            sendJson(exchange, 200, body);
        });
        server.createContext("/api/fatture/registrate", exchange -> {
            String body = "[{\"numeroFattura\":\"R-1\",\"dataEmissione\":\"2024-02-15\","
                    + "\"cliente\":{\"nome\":\"Laura\"},\"totale\":\"789.01\",\"stato\":\"PAGATA\",\"registrata\":true}]";
            sendJson(exchange, 200, body);
        });
        server.createContext("/api/pagamenti", exchange -> {
            String body = "[{\"metodo\":\"Carta\",\"importo\":\"89.10\",\"dataPagamento\":\"2024-03-05\","
                    + "\"fattura\":{\"numeroFattura\":\"F-1\"}}]";
            sendJson(exchange, 200, body);
        });
        server.createContext("/api/notifications", exchange -> {
            assertEquals("recipientId=55", exchange.getRequestURI().getQuery());
            String body = "[{\"title\":\"Reminder\",\"message\":\"Call client\",\"createdAt\":\"2024-04-01T09:30:00\",\"read\":true}]";
            sendJson(exchange, 200, body);
        });
        server.createContext("/api/agenda/dipendenti/5", exchange -> {
            String body = "[{\"start\":\"2024-05-01T08:00:00\",\"end\":\"2024-05-01T09:00:00\",\"title\":\"Meeting\",\"description\":\"Weekly sync\"}]";
            sendJson(exchange, 200, body);
        });
        server.createContext("/api/fatture/andamento", exchange -> {
            String body = "[{\"year\":2024,\"month\":5,\"total\":\"1500.00\"}]";
            sendJson(exchange, 200, body);
        });

        List<BackendGateway.InvoiceSnapshot> vendite = gateway.fetchFattureVendita();
        assertEquals(2, vendite.size());
        BackendGateway.InvoiceSnapshot firstSale = vendite.get(0);
        assertEquals("F-1", firstSale.number());
        assertEquals(LocalDate.of(2024, 1, 10), firstSale.issueDate());
        assertEquals("ACME", firstSale.customer());
        assertEquals(0, firstSale.total().compareTo(new java.math.BigDecimal("123.45")));
        assertEquals("EMESSA", firstSale.state());
        assertFalse(firstSale.registered());
        BackendGateway.InvoiceSnapshot secondSale = vendite.get(1);
        assertNull(secondSale.issueDate());
        assertEquals("Mario Rossi", secondSale.customer());
        assertEquals(0, secondSale.total().compareTo(java.math.BigDecimal.ZERO));
        assertNull(secondSale.state());
        assertTrue(secondSale.registered());

        List<BackendGateway.InvoiceSnapshot> registrate = gateway.fetchFattureRegistrate();
        assertEquals(1, registrate.size());
        BackendGateway.InvoiceSnapshot invoice = registrate.get(0);
        assertEquals("R-1", invoice.number());
        assertEquals(0, invoice.total().compareTo(new java.math.BigDecimal("789.01")));
        assertEquals("Laura", invoice.customer());
        assertEquals(LocalDate.of(2024, 2, 15), invoice.issueDate());
        assertEquals("PAGATA", invoice.state());
        assertTrue(invoice.registered());

        List<BackendGateway.PaymentSnapshot> payments = gateway.fetchPagamenti();
        assertEquals(1, payments.size());
        BackendGateway.PaymentSnapshot payment = payments.get(0);
        assertEquals("F-1", payment.invoiceNumber());
        assertEquals(0, payment.amount().compareTo(new java.math.BigDecimal("89.10")));
        assertEquals(LocalDate.of(2024, 3, 5), payment.paymentDate());
        assertEquals("Carta", payment.method());

        assertTrue(gateway.fetchNotifications(null).isEmpty());
        List<BackendGateway.NotificationSnapshot> notifications = gateway.fetchNotifications(55L);
        assertEquals(1, notifications.size());
        BackendGateway.NotificationSnapshot notification = notifications.get(0);
        assertEquals("Reminder", notification.title());
        assertEquals("Call client", notification.message());
        assertEquals(LocalDateTime.of(2024, 4, 1, 9, 30), notification.createdAt());
        assertTrue(notification.read());

        assertTrue(gateway.fetchAgenda(null).isEmpty());
        List<BackendGateway.AgendaSnapshot> agenda = gateway.fetchAgenda(5L);
        assertEquals(1, agenda.size());
        BackendGateway.AgendaSnapshot event = agenda.get(0);
        assertEquals(LocalDateTime.of(2024, 5, 1, 8, 0), event.start());
        assertEquals(LocalDateTime.of(2024, 5, 1, 9, 0), event.end());
        assertEquals("Meeting", event.title());
        assertEquals("Weekly sync", event.description());

        List<BackendGateway.MonthlyRevenueSnapshot> revenues = gateway.fetchMonthlyRevenue();
        assertEquals(1, revenues.size());
        BackendGateway.MonthlyRevenueSnapshot revenue = revenues.get(0);
        assertEquals(2024, revenue.year());
        assertEquals(5, revenue.month().intValue());
        assertEquals(0, revenue.total().compareTo(new java.math.BigDecimal("1500.00")));
    }

    @Test
    void shouldReturnEmptyCollectionsOnErrorResponses() {
        server.createContext("/api/dipendenti", exchange -> sendJson(exchange, 500, "error"));
        server.createContext("/api/fatture/vendita", exchange -> sendJson(exchange, 404, "missing"));
        server.createContext("/api/pagamenti", exchange -> sendJson(exchange, 204, ""));
        server.createContext("/api/notifications", exchange -> sendJson(exchange, 400, ""));
        server.createContext("/api/agenda/dipendenti/1", exchange -> sendJson(exchange, 503, ""));
        server.createContext("/api/fatture/andamento", exchange -> sendJson(exchange, 500, ""));

        assertTrue(gateway.fetchEmployees().isEmpty());
        assertTrue(gateway.fetchFattureVendita().isEmpty());
        assertTrue(gateway.fetchPagamenti().isEmpty());
        assertTrue(gateway.fetchNotifications(77L).isEmpty());
        assertTrue(gateway.fetchAgenda(1L).isEmpty());
        assertTrue(gateway.fetchMonthlyRevenue().isEmpty());
    }

    private void authenticateWithFixedToken() {
        server.createContext("/api/auth/login", exchange -> {
            readRequestBody(exchange);
            sendJson(exchange, 200, "\"fixed-token\"");
        });
        assertTrue(gateway.authenticate("admin", "pwd").isPresent());
    }

    private static void sendJson(HttpExchange exchange, int status, String body) throws IOException {
        byte[] payload = body == null ? new byte[0] : body.getBytes(StandardCharsets.UTF_8);
        if (body != null && !body.isEmpty()) {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
        }
        exchange.sendResponseHeaders(status, payload.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(payload);
        }
    }

    private static String readRequestBody(HttpExchange exchange) throws IOException {
        byte[] bytes = exchange.getRequestBody().readAllBytes();
        return new String(bytes, StandardCharsets.UTF_8);
    }
}

