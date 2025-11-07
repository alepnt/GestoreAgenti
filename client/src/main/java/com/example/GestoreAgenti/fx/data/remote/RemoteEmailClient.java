package com.example.GestoreAgenti.fx.data.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Client REST minimale che invia le email aziendali tramite il backend Spring.
 */
public class RemoteEmailClient {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() { };

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final URI baseUri;
    private final RemoteTaskScheduler taskScheduler;

    public RemoteEmailClient(RemoteTaskScheduler taskScheduler) {
        this(taskScheduler,
                HttpClient.newBuilder()
                        .connectTimeout(DEFAULT_TIMEOUT)
                        .build(),
                new ObjectMapper(),
                resolveBaseUri());
    }

    public RemoteEmailClient() {
        this(new RemoteTaskScheduler(Math.max(1, Runtime.getRuntime().availableProcessors())));
    }

    RemoteEmailClient(RemoteTaskScheduler taskScheduler,
                      HttpClient httpClient,
                      ObjectMapper objectMapper,
                      URI baseUri) {
        this.taskScheduler = Objects.requireNonNull(taskScheduler, "taskScheduler");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        this.baseUri = normalizeBaseUri(Objects.requireNonNull(baseUri, "baseUri"));
    }

    public CompletableFuture<Void> sendEmail(String from, String to, String subject, String body) {
        Objects.requireNonNull(from, "from");
        Objects.requireNonNull(to, "to");
        Objects.requireNonNull(subject, "subject");
        Objects.requireNonNull(body, "body");
        return taskScheduler.schedule(() -> {
            URI uri = baseUri.resolve("api/email");
            try {
                String payload = objectMapper.writeValueAsString(new EmailPayload(from, to, subject, body));
                HttpRequest request = HttpRequest.newBuilder(uri)
                        .timeout(DEFAULT_TIMEOUT)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload, StandardCharsets.UTF_8))
                        .build();
                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                        .thenCompose(response -> {
                            if (isSuccess(response.statusCode())) {
                                return CompletableFuture.completedFuture(null);
                            }
                            String errorMessage = extractErrorMessage(response);
                            return CompletableFuture.failedFuture(new IllegalStateException(errorMessage));
                        });
            } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
            }
        });
    }

    private static URI resolveBaseUri() {
        String baseUrl = System.getProperty("gestoreagenti.server.url");
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = System.getenv("GESTOREAGENTI_SERVER_URL");
        }
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "http://localhost:8081";
        }
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        try {
            return new URI(baseUrl);
        } catch (Exception e) {
            throw new IllegalStateException("URL del server non valido: " + baseUrl, e);
        }
    }

    private static URI normalizeBaseUri(URI baseUri) {
        String baseUrl = baseUri.toString();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        try {
            return new URI(baseUrl);
        } catch (Exception e) {
            throw new IllegalStateException("URL del server non valido: " + baseUrl, e);
        }
    }

    private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private String extractErrorMessage(HttpResponse<String> response) {
        String body = response.body();
        if (body != null && !body.isBlank()) {
            try {
                Map<String, Object> payload = objectMapper.readValue(body, MAP_TYPE);
                Object message = payload.get("message");
                if (message instanceof String str && !str.isBlank()) {
                    return str;
                }
            } catch (Exception ignored) {
                // il corpo non era JSON, ricadiamo sui messaggi standard
            }
            return body.strip();
        }
        return "Invio email fallito (HTTP %d)".formatted(response.statusCode());
    }

    private record EmailPayload(String from, String to, String subject, String body) { }
}
