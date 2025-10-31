package com.example.GestoreAgenti.fx.data.remote;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * Client REST minimale per scaricare il report Excel delle fatture dal backend.
 */
public class RemoteInvoiceClient {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient httpClient;
    private final URI baseUri;

    public RemoteInvoiceClient() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(DEFAULT_TIMEOUT)
                .build();
        this.baseUri = resolveBaseUri();
    }

    public CompletableFuture<byte[]> downloadInvoiceReport() {
        URI uri = baseUri.resolve("api/fatture/report");
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(DEFAULT_TIMEOUT)
                .GET()
                .header("Accept", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenCompose(response -> {
                    if (isSuccess(response.statusCode())) {
                        return CompletableFuture.completedFuture(response.body());
                    }
                    String message = "Download report fatture fallito (HTTP %d)".formatted(response.statusCode());
                    return CompletableFuture.failedFuture(new IllegalStateException(message));
                });
    }

    private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private URI resolveBaseUri() {
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
        } catch (URISyntaxException e) {
            throw new IllegalStateException("URL del server non valido: " + baseUrl, e);
        }
    }
}
