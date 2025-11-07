package com.example.GestoreAgenti.fx.data.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.Trailers;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class RemoteEmailClientTest {

    private static final String PROPERTY_NAME = "gestoreagenti.server.url";

    @AfterEach
    void cleanupProperty() {
        System.clearProperty(PROPERTY_NAME);
    }

    @Test
    void constructorRejectsInvalidBaseUrl() {
        System.setProperty(PROPERTY_NAME, "://bad url");
        IllegalStateException ex = assertThrows(IllegalStateException.class, RemoteEmailClient::new);
        assertTrue(ex.getMessage().contains("URL del server non valido"));
    }

    @Test
    void sendEmailRejectsNullArguments() {
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(2));
        assertThrows(NullPointerException.class, () -> client.sendEmail(null, "to", "subject", "body"));
        assertThrows(NullPointerException.class, () -> client.sendEmail("from", null, "subject", "body"));
        assertThrows(NullPointerException.class, () -> client.sendEmail("from", "to", null, "body"));
        assertThrows(NullPointerException.class, () -> client.sendEmail("from", "to", "subject", null));
    }

    @Test
    void sendEmailSuccessUsesInjectedHttpClient() {
        RecordingHttpClient httpClient = new RecordingHttpClient();
        httpClient.respondWith(new BasicResponse(200, "{}"));
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1),
                httpClient,
                new ObjectMapper(),
                URI.create("http://localhost:8081/"));

        client.sendEmail("from@example.com", "to@example.com", "Subject", "Body").join();

        assertNotNull(httpClient.lastRequest());
        assertEquals("http://localhost:8081/api/email", httpClient.lastRequest().uri().toString());
        assertEquals("application/json", httpClient.lastRequest().headers().firstValue("Content-Type").orElse(null));
    }

    @Test
    void sendEmailPropagatesServerErrorMessage() {
        RecordingHttpClient httpClient = new RecordingHttpClient();
        httpClient.respondWith(new BasicResponse(500, "{\"message\":\"Bloccato\"}"));
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1),
                httpClient,
                new ObjectMapper(),
                URI.create("http://localhost:8081/"));

        CompletableFuture<Void> result = client.sendEmail("from", "to", "subject", "body");
        assertTrue(result.isCompletedExceptionally());
        CompletionException failure = assertThrows(CompletionException.class, result::join);
        assertEquals("Bloccato", failure.getCause().getMessage());
    }

    @Test
    void extractErrorMessagePrefersJsonPayload() throws Exception {
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1));
        Method method = RemoteEmailClient.class.getDeclaredMethod("extractErrorMessage", HttpResponse.class);
        method.setAccessible(true);

        HttpResponse<String> jsonResponse = new BasicResponse(400, "{\"message\":\"Errore\"}");
        HttpResponse<String> plainResponse = new BasicResponse(400, " Testo in chiaro ");
        HttpResponse<String> emptyResponse = new BasicResponse(500, "");

        assertEquals("Errore", method.invoke(client, jsonResponse));
        assertEquals("Testo in chiaro", method.invoke(client, plainResponse));
        assertEquals("Invio email fallito (HTTP 500)", method.invoke(client, emptyResponse));
    }

    @SuppressWarnings("rawtypes")
    private record BasicResponse(int statusCode, String body) implements HttpResponse<String> {

        @Override
        public int statusCode() {
            return statusCode;
        }

        @Override
        public String body() {
            return body;
        }

        @Override
        public HttpRequest request() {
            return null;
        }

        @Override
        public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty();
        }

        @Override
        public HttpHeaders headers() {
            return HttpHeaders.of(Map.of(), (a, b) -> true);
        }

        @Override
        public URI uri() {
            return null;
        }

        @Override
        public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1;
        }

        @Override
        public Optional<java.net.ssl.SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public Optional<Duration> timeout() {
            return Optional.empty();
        }

        @Override
        public Optional<HttpResponse<Trailers>> trailers() {
            return Optional.empty();
        }

        @Override
        public Map<String, Object> attributes() {
            return Map.of();
        }
    }

    private static final class RecordingHttpClient extends HttpClient {
        private final AtomicReference<CompletableFuture<HttpResponse<String>>> nextResponse = new AtomicReference<>();
        private HttpRequest lastRequest;

        private RecordingHttpClient() {
            nextResponse.set(CompletableFuture.completedFuture(new BasicResponse(200, "{}")));
        }

        private void respondWith(HttpResponse<String> response) {
            nextResponse.set(CompletableFuture.completedFuture(response));
        }

        private HttpRequest lastRequest() {
            return lastRequest;
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler) {
            lastRequest = request;
            return nextResponse.get().thenApply(response -> {
                @SuppressWarnings("unchecked")
                HttpResponse<T> typed = (HttpResponse<T>) response;
                return typed;
            });
        }

        @Override
        public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.empty();
        }

        @Override
        public Redirect followRedirects() {
            return Redirect.NEVER;
        }

        @Override
        public Optional<java.net.CookieHandler> cookieHandler() {
            return Optional.empty();
        }

        @Override
        public Optional<java.net.ProxySelector> proxy() {
            return Optional.empty();
        }

        @Override
        public Optional<java.net.Authenticator> authenticator() {
            return Optional.empty();
        }

        @Override
        public java.net.ssl.SSLContext sslContext() {
            return null;
        }

        @Override
        public java.net.ssl.SSLParameters sslParameters() {
            return null;
        }

        @Override
        public Optional<Executor> executor() {
            return Optional.empty();
        }

        @Override
        public Version version() {
            return Version.HTTP_1_1;
        }
    }
}
