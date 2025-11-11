package com.example.GestoreAgenti.fx.data.remote; // Esegue: package com.example.GestoreAgenti.fx.data.remote;

import com.fasterxml.jackson.databind.ObjectMapper; // Esegue: import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach; // Esegue: import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test; // Esegue: import org.junit.jupiter.api.Test;

import java.lang.reflect.Method; // Esegue: import java.lang.reflect.Method;
import java.net.URI; // Esegue: import java.net.URI;
import java.net.http.HttpClient; // Esegue: import java.net.http.HttpClient;
import java.net.http.HttpHeaders; // Esegue: import java.net.http.HttpHeaders;
import java.net.http.HttpRequest; // Esegue: import java.net.http.HttpRequest;
import java.net.http.HttpResponse; // Esegue: import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler; // Esegue: import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.PushPromiseHandler; // Esegue: import java.net.http.HttpResponse.PushPromiseHandler;
import java.time.Duration; // Esegue: import java.time.Duration;
import java.util.Map; // Esegue: import java.util.Map;
import java.util.Optional; // Esegue: import java.util.Optional;
import java.util.concurrent.CompletableFuture; // Esegue: import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException; // Esegue: import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor; // Esegue: import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference; // Esegue: import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.SSLContext; // Esegue: import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters; // Esegue: import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession; // Esegue: import javax.net.ssl.SSLSession;

import static org.junit.jupiter.api.Assertions.*; // Esegue: import static org.junit.jupiter.api.Assertions.*;

class RemoteEmailClientTest { // Esegue: class RemoteEmailClientTest {

    private static final String PROPERTY_NAME = "gestoreagenti.server.url"; // Esegue: private static final String PROPERTY_NAME = "gestoreagenti.server.url";

    @AfterEach // Esegue: @AfterEach
    void cleanupProperty() { // Esegue: void cleanupProperty() {
        System.clearProperty(PROPERTY_NAME); // Esegue: System.clearProperty(PROPERTY_NAME);
    } // Esegue: }

    @Test // Esegue: @Test
    void constructorRejectsInvalidBaseUrl() { // Esegue: void constructorRejectsInvalidBaseUrl() {
        System.setProperty(PROPERTY_NAME, "://bad url");
        IllegalStateException ex = assertThrows(IllegalStateException.class, RemoteEmailClient::new); // Esegue: IllegalStateException ex = assertThrows(IllegalStateException.class, RemoteEmailClient::new);
        assertTrue(ex.getMessage().contains("URL del server non valido")); // Esegue: assertTrue(ex.getMessage().contains("URL del server non valido"));
    } // Esegue: }

    @Test // Esegue: @Test
    void sendEmailRejectsNullArguments() { // Esegue: void sendEmailRejectsNullArguments() {
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(2)); // Esegue: RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(2));
        assertThrows(NullPointerException.class, () -> client.sendEmail(null, "to", "subject", "body")); // Esegue: assertThrows(NullPointerException.class, () -> client.sendEmail(null, "to", "subject", "body"));
        assertThrows(NullPointerException.class, () -> client.sendEmail("from", null, "subject", "body")); // Esegue: assertThrows(NullPointerException.class, () -> client.sendEmail("from", null, "subject", "body"));
        assertThrows(NullPointerException.class, () -> client.sendEmail("from", "to", null, "body")); // Esegue: assertThrows(NullPointerException.class, () -> client.sendEmail("from", "to", null, "body"));
        assertThrows(NullPointerException.class, () -> client.sendEmail("from", "to", "subject", null)); // Esegue: assertThrows(NullPointerException.class, () -> client.sendEmail("from", "to", "subject", null));
    } // Esegue: }

    @Test // Esegue: @Test
    void sendEmailSuccessUsesInjectedHttpClient() { // Esegue: void sendEmailSuccessUsesInjectedHttpClient() {
        RecordingHttpClient httpClient = new RecordingHttpClient(); // Esegue: RecordingHttpClient httpClient = new RecordingHttpClient();
        httpClient.respondWith(new BasicResponse(200, "{}")); // Esegue: httpClient.respondWith(new BasicResponse(200, "{}"));
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1), // Esegue: RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1),
                httpClient, // Esegue: httpClient,
                new ObjectMapper(), // Esegue: new ObjectMapper(),
                URI.create("http://localhost:8081/"));

        client.sendEmail("from@example.com", "to@example.com", "Subject", "Body").join(); // Esegue: client.sendEmail("from@example.com", "to@example.com", "Subject", "Body").join();

        assertNotNull(httpClient.lastRequest()); // Esegue: assertNotNull(httpClient.lastRequest());
        assertEquals("http://localhost:8081/api/email", httpClient.lastRequest().uri().toString());
        assertEquals("application/json", httpClient.lastRequest().headers().firstValue("Content-Type").orElse(null)); // Esegue: assertEquals("application/json", httpClient.lastRequest().headers().firstValue("Content-Type").orElse(null));
    } // Esegue: }

    @Test // Esegue: @Test
    void sendEmailPropagatesServerErrorMessage() { // Esegue: void sendEmailPropagatesServerErrorMessage() {
        RecordingHttpClient httpClient = new RecordingHttpClient(); // Esegue: RecordingHttpClient httpClient = new RecordingHttpClient();
        httpClient.respondWith(new BasicResponse(500, "{\"message\":\"Bloccato\"}")); // Esegue: httpClient.respondWith(new BasicResponse(500, "{\"message\":\"Bloccato\"}"));
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1), // Esegue: RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1),
                httpClient, // Esegue: httpClient,
                new ObjectMapper(), // Esegue: new ObjectMapper(),
                URI.create("http://localhost:8081/"));

        CompletableFuture<Void> result = client.sendEmail("from", "to", "subject", "body"); // Esegue: CompletableFuture<Void> result = client.sendEmail("from", "to", "subject", "body");
        assertTrue(result.isCompletedExceptionally()); // Esegue: assertTrue(result.isCompletedExceptionally());
        CompletionException failure = assertThrows(CompletionException.class, result::join); // Esegue: CompletionException failure = assertThrows(CompletionException.class, result::join);
        assertEquals("Bloccato", failure.getCause().getMessage()); // Esegue: assertEquals("Bloccato", failure.getCause().getMessage());
    } // Esegue: }

    @Test // Esegue: @Test
    void extractErrorMessagePrefersJsonPayload() throws Exception { // Esegue: void extractErrorMessagePrefersJsonPayload() throws Exception {
        RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1)); // Esegue: RemoteEmailClient client = new RemoteEmailClient(new RemoteTaskScheduler(1));
        Method method = RemoteEmailClient.class.getDeclaredMethod("extractErrorMessage", HttpResponse.class); // Esegue: Method method = RemoteEmailClient.class.getDeclaredMethod("extractErrorMessage", HttpResponse.class);
        method.setAccessible(true); // Esegue: method.setAccessible(true);

        HttpResponse<String> jsonResponse = new BasicResponse(400, "{\"message\":\"Errore\"}"); // Esegue: HttpResponse<String> jsonResponse = new BasicResponse(400, "{\"message\":\"Errore\"}");
        HttpResponse<String> plainResponse = new BasicResponse(400, " Testo in chiaro "); // Esegue: HttpResponse<String> plainResponse = new BasicResponse(400, " Testo in chiaro ");
        HttpResponse<String> emptyResponse = new BasicResponse(500, ""); // Esegue: HttpResponse<String> emptyResponse = new BasicResponse(500, "");

        assertEquals("Errore", method.invoke(client, jsonResponse)); // Esegue: assertEquals("Errore", method.invoke(client, jsonResponse));
        assertEquals("Testo in chiaro", method.invoke(client, plainResponse)); // Esegue: assertEquals("Testo in chiaro", method.invoke(client, plainResponse));
        assertEquals("Invio email fallito (HTTP 500)", method.invoke(client, emptyResponse)); // Esegue: assertEquals("Invio email fallito (HTTP 500)", method.invoke(client, emptyResponse));
    } // Esegue: }

    private record BasicResponse(int statusCode, String body) implements HttpResponse<String> { // Esegue: private record BasicResponse(int statusCode, String body) implements HttpResponse<String> {

        @Override // Esegue: @Override
        public int statusCode() { // Esegue: public int statusCode() {
            return statusCode; // Esegue: return statusCode;
        } // Esegue: }

        @Override // Esegue: @Override
        public String body() { // Esegue: public String body() {
            return body; // Esegue: return body;
        } // Esegue: }

        @Override // Esegue: @Override
        public HttpRequest request() { // Esegue: public HttpRequest request() {
            return null; // Esegue: return null;
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<HttpResponse<String>> previousResponse() { // Esegue: public Optional<HttpResponse<String>> previousResponse() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        @Override // Esegue: @Override
        public HttpHeaders headers() { // Esegue: public HttpHeaders headers() {
            return HttpHeaders.of(Map.of(), (a, b) -> true); // Esegue: return HttpHeaders.of(Map.of(), (a, b) -> true);
        } // Esegue: }

        @Override // Esegue: @Override
        public URI uri() { // Esegue: public URI uri() {
            return null; // Esegue: return null;
        } // Esegue: }

        @Override // Esegue: @Override
        public HttpClient.Version version() { // Esegue: public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1; // Esegue: return HttpClient.Version.HTTP_1_1;
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<SSLSession> sslSession() { // Esegue: public Optional<SSLSession> sslSession() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }
    } // Esegue: }

    private static final class RecordingHttpClient extends HttpClient { // Esegue: private static final class RecordingHttpClient extends HttpClient {
        private final AtomicReference<CompletableFuture<HttpResponse<String>>> nextResponse = new AtomicReference<>(); // Esegue: private final AtomicReference<CompletableFuture<HttpResponse<String>>> nextResponse = new AtomicReference<>();
        private HttpRequest lastRequest; // Esegue: private HttpRequest lastRequest;

        private RecordingHttpClient() { // Esegue: private RecordingHttpClient() {
            nextResponse.set(CompletableFuture.completedFuture(new BasicResponse(200, "{}"))); // Esegue: nextResponse.set(CompletableFuture.completedFuture(new BasicResponse(200, "{}")));
        } // Esegue: }

        private void respondWith(HttpResponse<String> response) { // Esegue: private void respondWith(HttpResponse<String> response) {
            nextResponse.set(CompletableFuture.completedFuture(response)); // Esegue: nextResponse.set(CompletableFuture.completedFuture(response));
        } // Esegue: }

        private HttpRequest lastRequest() { // Esegue: private HttpRequest lastRequest() {
            return lastRequest; // Esegue: return lastRequest;
        } // Esegue: }

        @Override // Esegue: @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler) { // Esegue: public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler) {
            lastRequest = request; // Esegue: lastRequest = request;
            return nextResponse.get().thenApply(response -> { // Esegue: return nextResponse.get().thenApply(response -> {
                @SuppressWarnings("unchecked") // Esegue: @SuppressWarnings("unchecked")
                HttpResponse<T> typed = (HttpResponse<T>) response; // Esegue: HttpResponse<T> typed = (HttpResponse<T>) response;
                return typed; // Esegue: return typed;
            }); // Esegue: });
        } // Esegue: }

        @Override // Esegue: @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler, PushPromiseHandler<T> pushPromiseHandler) { // Esegue: public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler, PushPromiseHandler<T> pushPromiseHandler) {
            return sendAsync(request, handler); // Esegue: return sendAsync(request, handler);
        } // Esegue: }

        @Override // Esegue: @Override
        public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler) { // Esegue: public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler) {
            throw new UnsupportedOperationException(); // Esegue: throw new UnsupportedOperationException();
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<Duration> connectTimeout() { // Esegue: public Optional<Duration> connectTimeout() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        @Override // Esegue: @Override
        public Redirect followRedirects() { // Esegue: public Redirect followRedirects() {
            return Redirect.NEVER; // Esegue: return Redirect.NEVER;
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<java.net.CookieHandler> cookieHandler() { // Esegue: public Optional<java.net.CookieHandler> cookieHandler() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<java.net.ProxySelector> proxy() { // Esegue: public Optional<java.net.ProxySelector> proxy() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<java.net.Authenticator> authenticator() { // Esegue: public Optional<java.net.Authenticator> authenticator() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        @Override // Esegue: @Override
        public SSLContext sslContext() { // Esegue: public SSLContext sslContext() {
            return null; // Esegue: return null;
        } // Esegue: }

        @Override // Esegue: @Override
        public SSLParameters sslParameters() { // Esegue: public SSLParameters sslParameters() {
            return null; // Esegue: return null;
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<Executor> executor() { // Esegue: public Optional<Executor> executor() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        @Override // Esegue: @Override
        public HttpClient.Version version() { // Esegue: public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1; // Esegue: return HttpClient.Version.HTTP_1_1;
        } // Esegue: }
    } // Esegue: }
} // Esegue: }
