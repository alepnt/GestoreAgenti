package com.example.GestoreAgenti.fx.data.remote; // Esegue: package com.example.GestoreAgenti.fx.data.remote;

import com.fasterxml.jackson.core.type.TypeReference; // Esegue: import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper; // Esegue: import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI; // Esegue: import java.net.URI;
import java.net.http.HttpClient; // Esegue: import java.net.http.HttpClient;
import java.net.http.HttpRequest; // Esegue: import java.net.http.HttpRequest;
import java.net.http.HttpResponse; // Esegue: import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets; // Esegue: import java.nio.charset.StandardCharsets;
import java.time.Duration; // Esegue: import java.time.Duration;
import java.util.Map; // Esegue: import java.util.Map;
import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.concurrent.CompletableFuture; // Esegue: import java.util.concurrent.CompletableFuture;

/**
 * Client REST minimale che invia le email aziendali tramite il backend Spring.
 */
public class RemoteEmailClient { // Esegue: public class RemoteEmailClient {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5); // Esegue: private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() { }; // Esegue: private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() { };

    private final HttpClient httpClient; // Esegue: private final HttpClient httpClient;
    private final ObjectMapper objectMapper; // Esegue: private final ObjectMapper objectMapper;
    private final URI baseUri; // Esegue: private final URI baseUri;
    private final RemoteTaskScheduler taskScheduler; // Esegue: private final RemoteTaskScheduler taskScheduler;

    public RemoteEmailClient(RemoteTaskScheduler taskScheduler) { // Esegue: public RemoteEmailClient(RemoteTaskScheduler taskScheduler) {
        this(taskScheduler, // Esegue: this(taskScheduler,
                HttpClient.newBuilder() // Esegue: HttpClient.newBuilder()
                        .connectTimeout(DEFAULT_TIMEOUT) // Esegue: .connectTimeout(DEFAULT_TIMEOUT)
                        .build(), // Esegue: .build(),
                new ObjectMapper(), // Esegue: new ObjectMapper(),
                resolveBaseUri()); // Esegue: resolveBaseUri());
    } // Esegue: }

    public RemoteEmailClient() { // Esegue: public RemoteEmailClient() {
        this(new RemoteTaskScheduler(Math.max(1, Runtime.getRuntime().availableProcessors()))); // Esegue: this(new RemoteTaskScheduler(Math.max(1, Runtime.getRuntime().availableProcessors())));
    } // Esegue: }

    RemoteEmailClient(RemoteTaskScheduler taskScheduler, // Esegue: RemoteEmailClient(RemoteTaskScheduler taskScheduler,
                      HttpClient httpClient, // Esegue: HttpClient httpClient,
                      ObjectMapper objectMapper, // Esegue: ObjectMapper objectMapper,
                      URI baseUri) { // Esegue: URI baseUri) {
        this.taskScheduler = Objects.requireNonNull(taskScheduler, "taskScheduler"); // Esegue: this.taskScheduler = Objects.requireNonNull(taskScheduler, "taskScheduler");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient"); // Esegue: this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper"); // Esegue: this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        this.baseUri = normalizeBaseUri(Objects.requireNonNull(baseUri, "baseUri")); // Esegue: this.baseUri = normalizeBaseUri(Objects.requireNonNull(baseUri, "baseUri"));
    } // Esegue: }

    public CompletableFuture<Void> sendEmail(String from, String to, String subject, String body) { // Esegue: public CompletableFuture<Void> sendEmail(String from, String to, String subject, String body) {
        Objects.requireNonNull(from, "from"); // Esegue: Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to"); // Esegue: Objects.requireNonNull(to, "to");
        Objects.requireNonNull(subject, "subject"); // Esegue: Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(body, "body"); // Esegue: Objects.requireNonNull(body, "body");
        return taskScheduler.schedule(() -> { // Esegue: return taskScheduler.schedule(() -> {
            URI uri = baseUri.resolve("api/email"); // Esegue: URI uri = baseUri.resolve("api/email");
            try { // Esegue: try {
                String payload = objectMapper.writeValueAsString(new EmailPayload(from, to, subject, body)); // Esegue: String payload = objectMapper.writeValueAsString(new EmailPayload(from, to, subject, body));
                HttpRequest request = HttpRequest.newBuilder(uri) // Esegue: HttpRequest request = HttpRequest.newBuilder(uri)
                        .timeout(DEFAULT_TIMEOUT) // Esegue: .timeout(DEFAULT_TIMEOUT)
                        .header("Accept", "application/json") // Esegue: .header("Accept", "application/json")
                        .header("Content-Type", "application/json") // Esegue: .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8)) // Esegue: .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                        .build(); // Esegue: .build();
                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)) // Esegue: return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                        .thenCompose(response -> { // Esegue: .thenCompose(response -> {
                            if (isSuccess(response.statusCode())) { // Esegue: if (isSuccess(response.statusCode())) {
                                return CompletableFuture.completedFuture(null); // Esegue: return CompletableFuture.completedFuture(null);
                            } // Esegue: }
                            String errorMessage = extractErrorMessage(response); // Esegue: String errorMessage = extractErrorMessage(response);
                            return CompletableFuture.failedFuture(new IllegalStateException(errorMessage)); // Esegue: return CompletableFuture.failedFuture(new IllegalStateException(errorMessage));
                        }); // Esegue: });
            } catch (Exception e) { // Esegue: } catch (Exception e) {
                return CompletableFuture.failedFuture(e); // Esegue: return CompletableFuture.failedFuture(e);
            } // Esegue: }
        }); // Esegue: });
    } // Esegue: }

    private static URI resolveBaseUri() { // Esegue: private static URI resolveBaseUri() {
        String baseUrl = System.getProperty("gestoreagenti.server.url"); // Esegue: String baseUrl = System.getProperty("gestoreagenti.server.url");
        if (baseUrl == null || baseUrl.isBlank()) { // Esegue: if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = System.getenv("GESTOREAGENTI_SERVER_URL"); // Esegue: baseUrl = System.getenv("GESTOREAGENTI_SERVER_URL");
        } // Esegue: }
        if (baseUrl == null || baseUrl.isBlank()) { // Esegue: if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://localhost:8081";
        } // Esegue: }
        if (!baseUrl.endsWith("/")) { // Esegue: if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/"; // Esegue: baseUrl = baseUrl + "/";
        } // Esegue: }
        try { // Esegue: try {
            return new URI(baseUrl); // Esegue: return new URI(baseUrl);
        } catch (Exception e) { // Esegue: } catch (Exception e) {
            throw new IllegalStateException("URL del server non valido: " + baseUrl, e); // Esegue: throw new IllegalStateException("URL del server non valido: " + baseUrl, e);
        } // Esegue: }
    } // Esegue: }

    private static URI normalizeBaseUri(URI baseUri) { // Esegue: private static URI normalizeBaseUri(URI baseUri) {
        String baseUrl = baseUri.toString(); // Esegue: String baseUrl = baseUri.toString();
        if (!baseUrl.endsWith("/")) { // Esegue: if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/"; // Esegue: baseUrl = baseUrl + "/";
        } // Esegue: }
        try { // Esegue: try {
            return new URI(baseUrl); // Esegue: return new URI(baseUrl);
        } catch (Exception e) { // Esegue: } catch (Exception e) {
            throw new IllegalStateException("URL del server non valido: " + baseUrl, e); // Esegue: throw new IllegalStateException("URL del server non valido: " + baseUrl, e);
        } // Esegue: }
    } // Esegue: }

    private boolean isSuccess(int statusCode) { // Esegue: private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300; // Esegue: return statusCode >= 200 && statusCode < 300;
    } // Esegue: }

    private String extractErrorMessage(HttpResponse<String> response) { // Esegue: private String extractErrorMessage(HttpResponse<String> response) {
        String body = response.body(); // Esegue: String body = response.body();
        if (body != null && !body.isBlank()) { // Esegue: if (body != null && !body.isBlank()) {
            try { // Esegue: try {
                Map<String, Object> payload = objectMapper.readValue(body, MAP_TYPE); // Esegue: Map<String, Object> payload = objectMapper.readValue(body, MAP_TYPE);
                Object message = payload.get("message"); // Esegue: Object message = payload.get("message");
                if (message instanceof String str && !str.isBlank()) { // Esegue: if (message instanceof String str && !str.isBlank()) {
                    return str; // Esegue: return str;
                } // Esegue: }
            } catch (Exception ignored) { // Esegue: } catch (Exception ignored) {
                // il corpo non era JSON, ricadiamo sui messaggi standard
            } // Esegue: }
            return body.strip(); // Esegue: return body.strip();
        } // Esegue: }
        return "Invio email fallito (HTTP %d)".formatted(response.statusCode()); // Esegue: return "Invio email fallito (HTTP %d)".formatted(response.statusCode());
    } // Esegue: }

    private record EmailPayload(String from, String to, String subject, String body) { } // Esegue: private record EmailPayload(String from, String to, String subject, String body) { }
} // Esegue: }
