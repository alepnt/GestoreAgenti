package com.example.GestoreAgenti.fx.data.remote; // Esegue: package com.example.GestoreAgenti.fx.data.remote;

import com.example.GestoreAgenti.fx.model.ChatMessage; // Esegue: import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper; // Esegue: import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Esegue: import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach; // Esegue: import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test; // Esegue: import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor; // Esegue: import java.lang.reflect.Constructor;
import java.lang.reflect.Field; // Esegue: import java.lang.reflect.Field;
import java.net.URI; // Esegue: import java.net.URI;
import java.net.http.HttpClient; // Esegue: import java.net.http.HttpClient;
import java.net.http.HttpHeaders; // Esegue: import java.net.http.HttpHeaders;
import java.net.http.HttpRequest; // Esegue: import java.net.http.HttpRequest;
import java.net.http.HttpResponse; // Esegue: import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler; // Esegue: import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.PushPromiseHandler; // Esegue: import java.net.http.HttpResponse.PushPromiseHandler;
import java.net.http.WebSocket; // Esegue: import java.net.http.WebSocket;
import java.nio.ByteBuffer; // Esegue: import java.nio.ByteBuffer;
import java.time.Duration; // Esegue: import java.time.Duration;
import java.time.LocalDateTime; // Esegue: import java.time.LocalDateTime;
import java.util.List; // Esegue: import java.util.List;
import java.util.Map; // Esegue: import java.util.Map;
import java.util.Optional; // Esegue: import java.util.Optional;
import java.util.concurrent.CompletableFuture; // Esegue: import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException; // Esegue: import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor; // Esegue: import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean; // Esegue: import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference; // Esegue: import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function; // Esegue: import java.util.function.Function;

import javax.net.ssl.SSLContext; // Esegue: import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters; // Esegue: import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession; // Esegue: import javax.net.ssl.SSLSession;

import static org.junit.jupiter.api.Assertions.*; // Esegue: import static org.junit.jupiter.api.Assertions.*;

class RemoteChatClientTest { // Esegue: class RemoteChatClientTest {

    private static final String PROPERTY_NAME = "gestoreagenti.server.url"; // Esegue: private static final String PROPERTY_NAME = "gestoreagenti.server.url";

    @AfterEach // Esegue: @AfterEach
    void cleanupProperty() { // Esegue: void cleanupProperty() {
        System.clearProperty(PROPERTY_NAME); // Esegue: System.clearProperty(PROPERTY_NAME);
    } // Esegue: }

    @Test // Esegue: @Test
    void fetchTeamMessagesReturnsEmptyListForBlankTeam() { // Esegue: void fetchTeamMessagesReturnsEmptyListForBlankTeam() {
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2)); // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));
        assertTrue(client.fetchTeamMessages(" ").join().isEmpty()); // Esegue: assertTrue(client.fetchTeamMessages(" ").join().isEmpty());
    } // Esegue: }

    @Test // Esegue: @Test
    void fetchTeamMessagesParsesPayload() { // Esegue: void fetchTeamMessagesParsesPayload() {
        RecordingHttpClient httpClient = new RecordingHttpClient(); // Esegue: RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper(); // Esegue: ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2), httpClient, mapper, // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2), httpClient, mapper,
                URI.create("http://localhost:8081/"));

        String body = "[{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}]"; // Esegue: String body = "[{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}]";
        httpClient.respondWith(new BasicResponse(200, body)); // Esegue: httpClient.respondWith(new BasicResponse(200, body));

        List<ChatMessage> messages = client.fetchTeamMessages("Team Uno").join(); // Esegue: List<ChatMessage> messages = client.fetchTeamMessages("Team Uno").join();
        assertEquals(1, messages.size()); // Esegue: assertEquals(1, messages.size());
        assertEquals("Team Uno", messages.get(0).teamName()); // Esegue: assertEquals("Team Uno", messages.get(0).teamName());
        assertEquals("Anna", messages.get(0).sender()); // Esegue: assertEquals("Anna", messages.get(0).sender());
        assertEquals(LocalDateTime.parse("2024-01-02T10:15:30"), messages.get(0).timestamp()); // Esegue: assertEquals(LocalDateTime.parse("2024-01-02T10:15:30"), messages.get(0).timestamp());
        assertEquals("Ciao", messages.get(0).content()); // Esegue: assertEquals("Ciao", messages.get(0).content());
        assertEquals("GET", httpClient.lastRequest().method()); // Esegue: assertEquals("GET", httpClient.lastRequest().method());
        assertTrue(httpClient.lastRequest().uri().toString().endsWith("Team+Uno")); // Esegue: assertTrue(httpClient.lastRequest().uri().toString().endsWith("Team+Uno"));
    } // Esegue: }

    @Test // Esegue: @Test
    void sendTeamMessageRejectsMissingParameters() { // Esegue: void sendTeamMessageRejectsMissingParameters() {
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2)); // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));
        CompletableFuture<ChatMessage> future = client.sendTeamMessage("", "", ""); // Esegue: CompletableFuture<ChatMessage> future = client.sendTeamMessage("", "", "");
        assertTrue(future.isCompletedExceptionally()); // Esegue: assertTrue(future.isCompletedExceptionally());
    } // Esegue: }

    @Test // Esegue: @Test
    void sendTeamMessageParsesResponse() { // Esegue: void sendTeamMessageParsesResponse() {
        RecordingHttpClient httpClient = new RecordingHttpClient(); // Esegue: RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper(); // Esegue: ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(1), httpClient, mapper, // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(1), httpClient, mapper,
                URI.create("http://localhost:8081/"));

        String responseBody = "{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}"; // Esegue: String responseBody = "{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}";
        httpClient.respondWith(req -> new BasicResponse(200, responseBody)); // Esegue: httpClient.respondWith(req -> new BasicResponse(200, responseBody));

        ChatMessage result = client.sendTeamMessage("Team Uno", "Anna", "Ciao").join(); // Esegue: ChatMessage result = client.sendTeamMessage("Team Uno", "Anna", "Ciao").join();
        assertEquals("POST", httpClient.lastRequest().method()); // Esegue: assertEquals("POST", httpClient.lastRequest().method());
        assertEquals("Team Uno", result.teamName()); // Esegue: assertEquals("Team Uno", result.teamName());
        assertEquals("Anna", result.sender()); // Esegue: assertEquals("Anna", result.sender());
        assertEquals("Ciao", result.content()); // Esegue: assertEquals("Ciao", result.content());
    } // Esegue: }

    @Test // Esegue: @Test
    void sendTeamMessagePropagatesServerError() { // Esegue: void sendTeamMessagePropagatesServerError() {
        RecordingHttpClient httpClient = new RecordingHttpClient(); // Esegue: RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper(); // Esegue: ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(1), httpClient, mapper, // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(1), httpClient, mapper,
                URI.create("http://localhost:8081/"));

        httpClient.respondWith(req -> new BasicResponse(500, "Errore")); // Esegue: httpClient.respondWith(req -> new BasicResponse(500, "Errore"));

        CompletableFuture<ChatMessage> future = client.sendTeamMessage("Team Uno", "Anna", "Ciao"); // Esegue: CompletableFuture<ChatMessage> future = client.sendTeamMessage("Team Uno", "Anna", "Ciao");
        assertTrue(future.isCompletedExceptionally()); // Esegue: assertTrue(future.isCompletedExceptionally());
        CompletionException failure = assertThrows(CompletionException.class, future::join); // Esegue: CompletionException failure = assertThrows(CompletionException.class, future::join);
        assertTrue(failure.getCause() instanceof IllegalStateException); // Esegue: assertTrue(failure.getCause() instanceof IllegalStateException);
    } // Esegue: }

    @Test // Esegue: @Test
    void subscribeRejectsBlankTeam() { // Esegue: void subscribeRejectsBlankTeam() {
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2)); // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));
        CompletableFuture<AutoCloseable> subscription = client.subscribeToTeam(" ", msg -> { }, err -> { }); // Esegue: CompletableFuture<AutoCloseable> subscription = client.subscribeToTeam(" ", msg -> { }, err -> { });
        assertTrue(subscription.isCompletedExceptionally()); // Esegue: assertTrue(subscription.isCompletedExceptionally());
    } // Esegue: }

    @Test // Esegue: @Test
    void subscribeRegistersAndReceivesMessages() throws Exception { // Esegue: void subscribeRegistersAndReceivesMessages() throws Exception {
        RecordingHttpClient httpClient = new RecordingHttpClient(); // Esegue: RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper(); // Esegue: ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2), httpClient, mapper, // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2), httpClient, mapper,
                URI.create("https://host/app/"));

        AtomicReference<ChatMessage> lastMessage = new AtomicReference<>(); // Esegue: AtomicReference<ChatMessage> lastMessage = new AtomicReference<>();
        AtomicReference<Throwable> lastError = new AtomicReference<>(); // Esegue: AtomicReference<Throwable> lastError = new AtomicReference<>();
        AutoCloseable closer = client.subscribeToTeam(" Team Uno ", lastMessage::set, lastError::set).join(); // Esegue: AutoCloseable closer = client.subscribeToTeam(" Team Uno ", lastMessage::set, lastError::set).join();

        RecordingWebSocketBuilder builder = httpClient.webSocketBuilder(); // Esegue: RecordingWebSocketBuilder builder = httpClient.webSocketBuilder();
        assertEquals("wss", builder.lastUri().getScheme()); // Esegue: assertEquals("wss", builder.lastUri().getScheme());
        assertEquals("/app/ws/chat", builder.lastUri().getPath()); // Esegue: assertEquals("/app/ws/chat", builder.lastUri().getPath());
        assertEquals("team=Team+Uno", builder.lastUri().getQuery()); // Esegue: assertEquals("team=Team+Uno", builder.lastUri().getQuery());
        assertEquals(Duration.ofSeconds(5), builder.timeout()); // Esegue: assertEquals(Duration.ofSeconds(5), builder.timeout());

        builder.listener().onOpen(builder.webSocket()); // Esegue: builder.listener().onOpen(builder.webSocket());
        builder.listener().onText(builder.webSocket(), // Esegue: builder.listener().onText(builder.webSocket(),
                "{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}", // Esegue: "{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}",
                true); // Esegue: true);
        assertNotNull(lastMessage.get()); // Esegue: assertNotNull(lastMessage.get());
        assertNull(lastError.get()); // Esegue: assertNull(lastError.get());

        builder.listener().onText(builder.webSocket(), "non-json", true); // Esegue: builder.listener().onText(builder.webSocket(), "non-json", true);
        assertNotNull(lastError.get()); // Esegue: assertNotNull(lastError.get());

        closer.close(); // Esegue: closer.close();
        assertTrue(builder.webSocket().closed.get()); // Esegue: assertTrue(builder.webSocket().closed.get());
    } // Esegue: }

    @Test // Esegue: @Test
    void disconnectMethodsCloseWebSockets() throws Exception { // Esegue: void disconnectMethodsCloseWebSockets() throws Exception {
        System.setProperty(PROPERTY_NAME, "http://example.org/base");
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2)); // Esegue: RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));

        Field field = RemoteChatClient.class.getDeclaredField("activeConnections"); // Esegue: Field field = RemoteChatClient.class.getDeclaredField("activeConnections");
        field.setAccessible(true); // Esegue: field.setAccessible(true);
        @SuppressWarnings("unchecked") // Esegue: @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) field.get(client); // Esegue: Map<String, Object> map = (Map<String, Object>) field.get(client);

        RecordingWebSocket socketOne = new RecordingWebSocket(); // Esegue: RecordingWebSocket socketOne = new RecordingWebSocket();
        RecordingWebSocket socketTwo = new RecordingWebSocket(); // Esegue: RecordingWebSocket socketTwo = new RecordingWebSocket();
        map.put("team", newTeamConnection(socketOne)); // Esegue: map.put("team", newTeamConnection(socketOne));
        map.put("altro", newTeamConnection(socketTwo)); // Esegue: map.put("altro", newTeamConnection(socketTwo));

        client.disconnectFromTeam(" Team "); // Esegue: client.disconnectFromTeam(" Team ");
        assertTrue(socketOne.closed.get()); // Esegue: assertTrue(socketOne.closed.get());
        assertFalse(map.containsKey("team")); // Esegue: assertFalse(map.containsKey("team"));

        client.disconnectAll(); // Esegue: client.disconnectAll();
        assertTrue(socketTwo.closed.get()); // Esegue: assertTrue(socketTwo.closed.get());
        assertTrue(map.isEmpty()); // Esegue: assertTrue(map.isEmpty());
    } // Esegue: }

    private Object newTeamConnection(WebSocket webSocket) throws Exception { // Esegue: private Object newTeamConnection(WebSocket webSocket) throws Exception {
        Class<?> type = Class.forName("com.example.GestoreAgenti.fx.data.remote.RemoteChatClient$TeamConnection"); // Esegue: Class<?> type = Class.forName("com.example.GestoreAgenti.fx.data.remote.RemoteChatClient$TeamConnection");
        Constructor<?> constructor = type.getDeclaredConstructor(WebSocket.class); // Esegue: Constructor<?> constructor = type.getDeclaredConstructor(WebSocket.class);
        constructor.setAccessible(true); // Esegue: constructor.setAccessible(true);
        return constructor.newInstance(webSocket); // Esegue: return constructor.newInstance(webSocket);
    } // Esegue: }

    private ObjectMapper mapper() { // Esegue: private ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper(); // Esegue: ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Esegue: mapper.registerModule(new JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Esegue: mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper; // Esegue: return mapper;
    } // Esegue: }

    private record BasicResponse(int statusCode, String body) implements HttpResponse<String> { // Esegue: private record BasicResponse(int statusCode, String body) implements HttpResponse<String> {

        @Override // Esegue: @Override
        public int statusCode() { // Esegue: public int statusCode() {
            return statusCode; // Esegue: return statusCode;
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
        public String body() { // Esegue: public String body() {
            return body; // Esegue: return body;
        } // Esegue: }

        @Override // Esegue: @Override
        public HttpClient.Version version() { // Esegue: public HttpClient.Version version() {
            return HttpClient.Version.HTTP_1_1; // Esegue: return HttpClient.Version.HTTP_1_1;
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<SSLSession> sslSession() { // Esegue: public Optional<SSLSession> sslSession() {
            return Optional.empty(); // Esegue: return Optional.empty();
        } // Esegue: }

        @Override // Esegue: @Override
        public URI uri() { // Esegue: public URI uri() {
            return null; // Esegue: return null;
        } // Esegue: }
    } // Esegue: }

    private static final class RecordingHttpClient extends HttpClient { // Esegue: private static final class RecordingHttpClient extends HttpClient {
        private final AtomicReference<Function<HttpRequest, CompletableFuture<HttpResponse<String>>>> handler = // Esegue: private final AtomicReference<Function<HttpRequest, CompletableFuture<HttpResponse<String>>>> handler =
                new AtomicReference<>(req -> CompletableFuture.failedFuture(new AssertionError("No response configured"))); // Esegue: new AtomicReference<>(req -> CompletableFuture.failedFuture(new AssertionError("No response configured")));
        private final RecordingWebSocketBuilder webSocketBuilder = new RecordingWebSocketBuilder(); // Esegue: private final RecordingWebSocketBuilder webSocketBuilder = new RecordingWebSocketBuilder();
        private HttpRequest lastRequest; // Esegue: private HttpRequest lastRequest;

        private void respondWith(HttpResponse<String> response) { // Esegue: private void respondWith(HttpResponse<String> response) {
            handler.set(req -> CompletableFuture.completedFuture(response)); // Esegue: handler.set(req -> CompletableFuture.completedFuture(response));
        } // Esegue: }

        private void respondWith(Function<HttpRequest, HttpResponse<String>> responseFactory) { // Esegue: private void respondWith(Function<HttpRequest, HttpResponse<String>> responseFactory) {
            handler.set(req -> CompletableFuture.completedFuture(responseFactory.apply(req))); // Esegue: handler.set(req -> CompletableFuture.completedFuture(responseFactory.apply(req)));
        } // Esegue: }

        private HttpRequest lastRequest() { // Esegue: private HttpRequest lastRequest() {
            return lastRequest; // Esegue: return lastRequest;
        } // Esegue: }

        private RecordingWebSocketBuilder webSocketBuilder() { // Esegue: private RecordingWebSocketBuilder webSocketBuilder() {
            return webSocketBuilder; // Esegue: return webSocketBuilder;
        } // Esegue: }

        @Override // Esegue: @Override
        public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler) { // Esegue: public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler) {
            throw new UnsupportedOperationException(); // Esegue: throw new UnsupportedOperationException();
        } // Esegue: }

        @Override // Esegue: @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler) { // Esegue: public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler) {
            lastRequest = request; // Esegue: lastRequest = request;
            return this.handler.get().apply(request).thenApply(response -> { // Esegue: return this.handler.get().apply(request).thenApply(response -> {
                @SuppressWarnings("unchecked") // Esegue: @SuppressWarnings("unchecked")
                HttpResponse<T> cast = (HttpResponse<T>) response; // Esegue: HttpResponse<T> cast = (HttpResponse<T>) response;
                return cast; // Esegue: return cast;
            }); // Esegue: });
        } // Esegue: }

        @Override // Esegue: @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler, PushPromiseHandler<T> pushPromiseHandler) { // Esegue: public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler, PushPromiseHandler<T> pushPromiseHandler) {
            return sendAsync(request, handler); // Esegue: return sendAsync(request, handler);
        } // Esegue: }

        @Override // Esegue: @Override
        public WebSocket.Builder newWebSocketBuilder() { // Esegue: public WebSocket.Builder newWebSocketBuilder() {
            return webSocketBuilder; // Esegue: return webSocketBuilder;
        } // Esegue: }

        @Override // Esegue: @Override
        public Optional<Duration> connectTimeout() { // Esegue: public Optional<Duration> connectTimeout() {
            return Optional.of(Duration.ofSeconds(5)); // Esegue: return Optional.of(Duration.ofSeconds(5));
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

    private static final class RecordingWebSocketBuilder implements WebSocket.Builder { // Esegue: private static final class RecordingWebSocketBuilder implements WebSocket.Builder {
        private final RecordingWebSocket webSocket = new RecordingWebSocket(); // Esegue: private final RecordingWebSocket webSocket = new RecordingWebSocket();
        private final AtomicReference<URI> lastUri = new AtomicReference<>(); // Esegue: private final AtomicReference<URI> lastUri = new AtomicReference<>();
        private final AtomicReference<WebSocket.Listener> listener = new AtomicReference<>(); // Esegue: private final AtomicReference<WebSocket.Listener> listener = new AtomicReference<>();
        private final AtomicReference<Duration> timeout = new AtomicReference<>(); // Esegue: private final AtomicReference<Duration> timeout = new AtomicReference<>();

        private RecordingWebSocket webSocket() { // Esegue: private RecordingWebSocket webSocket() {
            return webSocket; // Esegue: return webSocket;
        } // Esegue: }

        private URI lastUri() { // Esegue: private URI lastUri() {
            return lastUri.get(); // Esegue: return lastUri.get();
        } // Esegue: }

        private WebSocket.Listener listener() { // Esegue: private WebSocket.Listener listener() {
            return listener.get(); // Esegue: return listener.get();
        } // Esegue: }

        private Duration timeout() { // Esegue: private Duration timeout() {
            return timeout.get(); // Esegue: return timeout.get();
        } // Esegue: }

        @Override // Esegue: @Override
        public WebSocket.Builder header(String name, String value) { // Esegue: public WebSocket.Builder header(String name, String value) {
            return this; // Esegue: return this;
        } // Esegue: }

        @Override // Esegue: @Override
        public WebSocket.Builder connectTimeout(Duration duration) { // Esegue: public WebSocket.Builder connectTimeout(Duration duration) {
            timeout.set(duration); // Esegue: timeout.set(duration);
            return this; // Esegue: return this;
        } // Esegue: }

        @Override // Esegue: @Override
        public WebSocket.Builder subprotocols(String... subprotocols) { // Esegue: public WebSocket.Builder subprotocols(String... subprotocols) {
            return this; // Esegue: return this;
        } // Esegue: }

        @Override // Esegue: @Override
        public CompletableFuture<WebSocket> buildAsync(URI uri, WebSocket.Listener listener) { // Esegue: public CompletableFuture<WebSocket> buildAsync(URI uri, WebSocket.Listener listener) {
            lastUri.set(uri); // Esegue: lastUri.set(uri);
            this.listener.set(listener); // Esegue: this.listener.set(listener);
            return CompletableFuture.completedFuture(webSocket); // Esegue: return CompletableFuture.completedFuture(webSocket);
        } // Esegue: }
    } // Esegue: }

    private static final class RecordingWebSocket implements WebSocket { // Esegue: private static final class RecordingWebSocket implements WebSocket {
        private final AtomicBoolean closed = new AtomicBoolean(); // Esegue: private final AtomicBoolean closed = new AtomicBoolean();

        @Override // Esegue: @Override
        public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) { // Esegue: public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {
            return CompletableFuture.completedFuture(this); // Esegue: return CompletableFuture.completedFuture(this);
        } // Esegue: }

        @Override // Esegue: @Override
        public CompletableFuture<WebSocket> sendBinary(ByteBuffer data, boolean last) { // Esegue: public CompletableFuture<WebSocket> sendBinary(ByteBuffer data, boolean last) {
            return CompletableFuture.completedFuture(this); // Esegue: return CompletableFuture.completedFuture(this);
        } // Esegue: }

        @Override // Esegue: @Override
        public CompletableFuture<WebSocket> sendPing(ByteBuffer message) { // Esegue: public CompletableFuture<WebSocket> sendPing(ByteBuffer message) {
            return CompletableFuture.completedFuture(this); // Esegue: return CompletableFuture.completedFuture(this);
        } // Esegue: }

        @Override // Esegue: @Override
        public CompletableFuture<WebSocket> sendPong(ByteBuffer message) { // Esegue: public CompletableFuture<WebSocket> sendPong(ByteBuffer message) {
            return CompletableFuture.completedFuture(this); // Esegue: return CompletableFuture.completedFuture(this);
        } // Esegue: }

        @Override // Esegue: @Override
        public CompletableFuture<WebSocket> sendClose(int statusCode, String reason) { // Esegue: public CompletableFuture<WebSocket> sendClose(int statusCode, String reason) {
            closed.set(true); // Esegue: closed.set(true);
            return CompletableFuture.completedFuture(this); // Esegue: return CompletableFuture.completedFuture(this);
        } // Esegue: }

        @Override // Esegue: @Override
        public void request(long n) { // Esegue: public void request(long n) {
        } // Esegue: }

        @Override // Esegue: @Override
        public String getSubprotocol() { // Esegue: public String getSubprotocol() {
            return null; // Esegue: return null;
        } // Esegue: }

        @Override // Esegue: @Override
        public boolean isOutputClosed() { // Esegue: public boolean isOutputClosed() {
            return false; // Esegue: return false;
        } // Esegue: }

        @Override // Esegue: @Override
        public boolean isInputClosed() { // Esegue: public boolean isInputClosed() {
            return false; // Esegue: return false;
        } // Esegue: }

        @Override // Esegue: @Override
        public void abort() { // Esegue: public void abort() {
            closed.set(true); // Esegue: closed.set(true);
        } // Esegue: }
    } // Esegue: }
} // Esegue: }
