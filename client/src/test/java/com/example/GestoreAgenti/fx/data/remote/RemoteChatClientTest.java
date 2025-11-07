package com.example.GestoreAgenti.fx.data.remote;

import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.Trailers;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class RemoteChatClientTest {

    private static final String PROPERTY_NAME = "gestoreagenti.server.url";

    @AfterEach
    void cleanupProperty() {
        System.clearProperty(PROPERTY_NAME);
    }

    @Test
    void fetchTeamMessagesReturnsEmptyListForBlankTeam() {
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));
        assertTrue(client.fetchTeamMessages(" ").join().isEmpty());
    }

    @Test
    void fetchTeamMessagesParsesPayload() {
        RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2), httpClient, mapper,
                URI.create("http://localhost:8081/"));

        String body = "[{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}]";
        httpClient.respondWith(new BasicResponse(200, body));

        List<ChatMessage> messages = client.fetchTeamMessages("Team Uno").join();
        assertEquals(1, messages.size());
        assertEquals("Team Uno", messages.get(0).teamName());
        assertEquals("Anna", messages.get(0).sender());
        assertEquals(LocalDateTime.parse("2024-01-02T10:15:30"), messages.get(0).timestamp());
        assertEquals("Ciao", messages.get(0).content());
        assertEquals("GET", httpClient.lastRequest().method());
        assertTrue(httpClient.lastRequest().uri().toString().endsWith("Team+Uno"));
    }

    @Test
    void sendTeamMessageRejectsMissingParameters() {
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));
        CompletableFuture<ChatMessage> future = client.sendTeamMessage("", "", "");
        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void sendTeamMessageParsesResponse() {
        RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(1), httpClient, mapper,
                URI.create("http://localhost:8081/"));

        String responseBody = "{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}";
        httpClient.respondWith(req -> new BasicResponse(200, responseBody));

        ChatMessage result = client.sendTeamMessage("Team Uno", "Anna", "Ciao").join();
        assertEquals("POST", httpClient.lastRequest().method());
        assertEquals("Team Uno", result.teamName());
        assertEquals("Anna", result.sender());
        assertEquals("Ciao", result.content());
    }

    @Test
    void sendTeamMessagePropagatesServerError() {
        RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(1), httpClient, mapper,
                URI.create("http://localhost:8081/"));

        httpClient.respondWith(req -> new BasicResponse(500, "Errore"));

        CompletableFuture<ChatMessage> future = client.sendTeamMessage("Team Uno", "Anna", "Ciao");
        assertTrue(future.isCompletedExceptionally());
        CompletionException failure = assertThrows(CompletionException.class, future::join);
        assertTrue(failure.getCause() instanceof IllegalStateException);
    }

    @Test
    void subscribeRejectsBlankTeam() {
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));
        CompletableFuture<AutoCloseable> subscription = client.subscribeToTeam(" ", msg -> { }, err -> { });
        assertTrue(subscription.isCompletedExceptionally());
    }

    @Test
    void subscribeRegistersAndReceivesMessages() throws Exception {
        RecordingHttpClient httpClient = new RecordingHttpClient();
        ObjectMapper mapper = mapper();
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2), httpClient, mapper,
                URI.create("https://host/app/"));

        AtomicReference<ChatMessage> lastMessage = new AtomicReference<>();
        AtomicReference<Throwable> lastError = new AtomicReference<>();
        AutoCloseable closer = client.subscribeToTeam(" Team Uno ", lastMessage::set, lastError::set).join();

        RecordingWebSocketBuilder builder = httpClient.webSocketBuilder();
        assertEquals("wss", builder.lastUri().getScheme());
        assertEquals("/app/ws/chat", builder.lastUri().getPath());
        assertEquals("team=Team+Uno", builder.lastUri().getQuery());
        assertEquals(Duration.ofSeconds(5), builder.timeout());

        builder.listener().onOpen(builder.webSocket());
        builder.listener().onText(builder.webSocket(),
                "{\"teamName\":\"Team Uno\",\"sender\":\"Anna\",\"timestamp\":\"2024-01-02T10:15:30\",\"content\":\"Ciao\"}",
                true);
        assertNotNull(lastMessage.get());
        assertNull(lastError.get());

        builder.listener().onText(builder.webSocket(), "non-json", true);
        assertNotNull(lastError.get());

        closer.close();
        assertTrue(builder.webSocket().closed.get());
    }

    @Test
    void disconnectMethodsCloseWebSockets() throws Exception {
        System.setProperty(PROPERTY_NAME, "http://example.org/base");
        RemoteChatClient client = new RemoteChatClient(new RemoteTaskScheduler(2));

        Field field = RemoteChatClient.class.getDeclaredField("activeConnections");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) field.get(client);

        RecordingWebSocket socketOne = new RecordingWebSocket();
        RecordingWebSocket socketTwo = new RecordingWebSocket();
        map.put("team", newTeamConnection(socketOne));
        map.put("altro", newTeamConnection(socketTwo));

        client.disconnectFromTeam(" Team ");
        assertTrue(socketOne.closed.get());
        assertFalse(map.containsKey("team"));

        client.disconnectAll();
        assertTrue(socketTwo.closed.get());
        assertTrue(map.isEmpty());
    }

    private Object newTeamConnection(WebSocket webSocket) throws Exception {
        Class<?> type = Class.forName("com.example.GestoreAgenti.fx.data.remote.RemoteChatClient$TeamConnection");
        Constructor<?> constructor = type.getDeclaredConstructor(WebSocket.class);
        constructor.setAccessible(true);
        return constructor.newInstance(webSocket);
    }

    private ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    private record BasicResponse(int statusCode, String body) implements HttpResponse<String> {

        @Override
        public int statusCode() {
            return statusCode;
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
        public String body() {
            return body;
        }

        @Override
        public Optional<Duration> timeout() {
            return Optional.empty();
        }

        @Override
        public Version version() {
            return Version.HTTP_1_1;
        }

        @Override
        public Optional<java.net.ssl.SSLSession> sslSession() {
            return Optional.empty();
        }

        @Override
        public URI uri() {
            return null;
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
        private final AtomicReference<Function<HttpRequest, CompletableFuture<HttpResponse<String>>>> handler =
                new AtomicReference<>(req -> CompletableFuture.failedFuture(new AssertionError("No response configured")));
        private final RecordingWebSocketBuilder webSocketBuilder = new RecordingWebSocketBuilder();
        private HttpRequest lastRequest;

        private void respondWith(HttpResponse<String> response) {
            handler.set(req -> CompletableFuture.completedFuture(response));
        }

        private void respondWith(Function<HttpRequest, HttpResponse<String>> responseFactory) {
            handler.set(req -> CompletableFuture.completedFuture(responseFactory.apply(req)));
        }

        private HttpRequest lastRequest() {
            return lastRequest;
        }

        private RecordingWebSocketBuilder webSocketBuilder() {
            return webSocketBuilder;
        }

        @Override
        public <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> responseBodyHandler) {
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> CompletableFuture<HttpResponse<T>> sendAsync(HttpRequest request, BodyHandler<T> handler) {
            lastRequest = request;
            return this.handler.get().apply(request).thenApply(response -> {
                @SuppressWarnings("unchecked")
                HttpResponse<T> cast = (HttpResponse<T>) response;
                return cast;
            });
        }

        @Override
        public WebSocket.Builder newWebSocketBuilder() {
            return webSocketBuilder;
        }

        @Override
        public Optional<Duration> connectTimeout() {
            return Optional.of(Duration.ofSeconds(5));
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

    private static final class RecordingWebSocketBuilder implements WebSocket.Builder {
        private final RecordingWebSocket webSocket = new RecordingWebSocket();
        private final AtomicReference<URI> lastUri = new AtomicReference<>();
        private final AtomicReference<WebSocket.Listener> listener = new AtomicReference<>();
        private final AtomicReference<Duration> timeout = new AtomicReference<>();

        private RecordingWebSocket webSocket() {
            return webSocket;
        }

        private URI lastUri() {
            return lastUri.get();
        }

        private WebSocket.Listener listener() {
            return listener.get();
        }

        private Duration timeout() {
            return timeout.get();
        }

        @Override
        public WebSocket.Builder header(String name, String value) {
            return this;
        }

        @Override
        public WebSocket.Builder connectTimeout(Duration duration) {
            timeout.set(duration);
            return this;
        }

        @Override
        public WebSocket.Builder subprotocols(String... subprotocols) {
            return this;
        }

        @Override
        public CompletableFuture<WebSocket> buildAsync(URI uri, WebSocket.Listener listener) {
            lastUri.set(uri);
            this.listener.set(listener);
            return CompletableFuture.completedFuture(webSocket);
        }
    }

    private static final class RecordingWebSocket implements WebSocket {
        private final AtomicBoolean closed = new AtomicBoolean();

        @Override
        public CompletableFuture<WebSocket> sendText(CharSequence data, boolean last) {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendBinary(ByteBuffer data, boolean last) {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendPing(ByteBuffer message) {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendPong(ByteBuffer message) {
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public CompletableFuture<WebSocket> sendClose(int statusCode, String reason) {
            closed.set(true);
            return CompletableFuture.completedFuture(this);
        }

        @Override
        public void request(long n) {
        }

        @Override
        public String getSubprotocol() {
            return null;
        }

        @Override
        public boolean isOutputClosed() {
            return false;
        }

        @Override
        public boolean isInputClosed() {
            return false;
        }

        @Override
        public void abort() {
            closed.set(true);
        }
    }
}
