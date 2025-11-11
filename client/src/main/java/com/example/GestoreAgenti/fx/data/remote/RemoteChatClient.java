package com.example.GestoreAgenti.fx.data.remote; // Esegue: package com.example.GestoreAgenti.fx.data.remote;

import com.example.GestoreAgenti.fx.model.ChatMessage; // Esegue: import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.fasterxml.jackson.core.type.TypeReference; // Esegue: import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper; // Esegue: import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature; // Esegue: import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule; // Esegue: import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI; // Esegue: import java.net.URI;
import java.net.URISyntaxException; // Esegue: import java.net.URISyntaxException;
import java.net.URLEncoder; // Esegue: import java.net.URLEncoder;
import java.net.http.HttpClient; // Esegue: import java.net.http.HttpClient;
import java.net.http.HttpRequest; // Esegue: import java.net.http.HttpRequest;
import java.net.http.HttpResponse; // Esegue: import java.net.http.HttpResponse;
import java.net.http.WebSocket; // Esegue: import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets; // Esegue: import java.nio.charset.StandardCharsets;
import java.time.Duration; // Esegue: import java.time.Duration;
import java.util.List; // Esegue: import java.util.List;
import java.util.Locale; // Esegue: import java.util.Locale;
import java.util.Map; // Esegue: import java.util.Map;
import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.concurrent.CompletableFuture; // Esegue: import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException; // Esegue: import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage; // Esegue: import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap; // Esegue: import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer; // Esegue: import java.util.function.Consumer;

/**
 * Client responsabile di sincronizzare i messaggi di chat con il server Spring Boot.
 */
public class RemoteChatClient { // Esegue: public class RemoteChatClient {

    private static final TypeReference<List<ChatMessage>> CHAT_LIST_TYPE = new TypeReference<>() { }; // Esegue: private static final TypeReference<List<ChatMessage>> CHAT_LIST_TYPE = new TypeReference<>() { };
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5); // Esegue: private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    private final HttpClient httpClient; // Esegue: private final HttpClient httpClient;
    private final ObjectMapper objectMapper; // Esegue: private final ObjectMapper objectMapper;
    private final URI baseUri; // Esegue: private final URI baseUri;
    private final Map<String, TeamConnection> activeConnections = new ConcurrentHashMap<>(); // Esegue: private final Map<String, TeamConnection> activeConnections = new ConcurrentHashMap<>();
    private final RemoteTaskScheduler taskScheduler; // Esegue: private final RemoteTaskScheduler taskScheduler;
    private volatile String authToken;

    public RemoteChatClient(RemoteTaskScheduler taskScheduler) { // Esegue: public RemoteChatClient(RemoteTaskScheduler taskScheduler) {
        this(taskScheduler, // Esegue: this(taskScheduler,
                HttpClient.newBuilder() // Esegue: HttpClient.newBuilder()
                        .connectTimeout(DEFAULT_TIMEOUT) // Esegue: .connectTimeout(DEFAULT_TIMEOUT)
                        .build(), // Esegue: .build(),
                defaultObjectMapper(), // Esegue: defaultObjectMapper(),
                resolveBaseUri()); // Esegue: resolveBaseUri());
    } // Esegue: }

    public RemoteChatClient() { // Esegue: public RemoteChatClient() {
        this(new RemoteTaskScheduler(Math.max(1, Runtime.getRuntime().availableProcessors()))); // Esegue: this(new RemoteTaskScheduler(Math.max(1, Runtime.getRuntime().availableProcessors())));
    } // Esegue: }

    RemoteChatClient(RemoteTaskScheduler taskScheduler, // Esegue: RemoteChatClient(RemoteTaskScheduler taskScheduler,
                     HttpClient httpClient, // Esegue: HttpClient httpClient,
                     ObjectMapper objectMapper, // Esegue: ObjectMapper objectMapper,
                     URI baseUri) { // Esegue: URI baseUri) {
        this.taskScheduler = Objects.requireNonNull(taskScheduler, "taskScheduler"); // Esegue: this.taskScheduler = Objects.requireNonNull(taskScheduler, "taskScheduler");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient"); // Esegue: this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper"); // Esegue: this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        this.baseUri = normalizeBaseUri(Objects.requireNonNull(baseUri, "baseUri")); // Esegue: this.baseUri = normalizeBaseUri(Objects.requireNonNull(baseUri, "baseUri"));
    } // Esegue: }

    public CompletableFuture<List<ChatMessage>> fetchTeamMessages(String teamName) { // Esegue: public CompletableFuture<List<ChatMessage>> fetchTeamMessages(String teamName) {
        return taskScheduler.schedule(() -> { // Esegue: return taskScheduler.schedule(() -> {
            if (teamName == null || teamName.isBlank()) { // Esegue: if (teamName == null || teamName.isBlank()) {
                return CompletableFuture.completedFuture(List.of()); // Esegue: return CompletableFuture.completedFuture(List.of());
            } // Esegue: }
            String encodedTeam = URLEncoder.encode(teamName, StandardCharsets.UTF_8); // Esegue: String encodedTeam = URLEncoder.encode(teamName, StandardCharsets.UTF_8);
            URI uri = baseUri.resolve("api/chat/" + encodedTeam); // Esegue: URI uri = baseUri.resolve("api/chat/" + encodedTeam);
            HttpRequest.Builder builder = HttpRequest.newBuilder(uri) // Esegue: HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                    .header("Accept", "application/json"); // Esegue: .header("Accept", "application/json");
            applyAuthorization(builder);
            HttpRequest request = builder.GET().build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()) // Esegue: return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenCompose(response -> { // Esegue: .thenCompose(response -> {
                        if (isSuccess(response.statusCode())) { // Esegue: if (isSuccess(response.statusCode())) {
                            try { // Esegue: try {
                                List<ChatMessage> messages = objectMapper.readValue(response.body(), CHAT_LIST_TYPE); // Esegue: List<ChatMessage> messages = objectMapper.readValue(response.body(), CHAT_LIST_TYPE);
                                return CompletableFuture.completedFuture(messages); // Esegue: return CompletableFuture.completedFuture(messages);
                            } catch (Exception e) { // Esegue: } catch (Exception e) {
                                return CompletableFuture.failedFuture(e); // Esegue: return CompletableFuture.failedFuture(e);
                            } // Esegue: }
                        } // Esegue: }
                        return CompletableFuture.failedFuture(new IllegalStateException( // Esegue: return CompletableFuture.failedFuture(new IllegalStateException(
                                "Unexpected response status: " + response.statusCode())); // Esegue: "Unexpected response status: " + response.statusCode()));
                    }); // Esegue: });
        }); // Esegue: });
    } // Esegue: }

    public CompletableFuture<ChatMessage> sendTeamMessage(String teamName, String sender, String content) { // Esegue: public CompletableFuture<ChatMessage> sendTeamMessage(String teamName, String sender, String content) {
        return taskScheduler.schedule(() -> { // Esegue: return taskScheduler.schedule(() -> {
            if (teamName == null || teamName.isBlank() // Esegue: if (teamName == null || teamName.isBlank()
                    || sender == null || sender.isBlank() // Esegue: || sender == null || sender.isBlank()
                    || content == null || content.isBlank()) { // Esegue: || content == null || content.isBlank()) {
                return CompletableFuture.failedFuture( // Esegue: return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Team, mittente e contenuto devono essere valorizzati")); // Esegue: new IllegalArgumentException("Team, mittente e contenuto devono essere valorizzati"));
            } // Esegue: }
            String encodedTeam = URLEncoder.encode(teamName, StandardCharsets.UTF_8); // Esegue: String encodedTeam = URLEncoder.encode(teamName, StandardCharsets.UTF_8);
            URI uri = baseUri.resolve("api/chat/" + encodedTeam); // Esegue: URI uri = baseUri.resolve("api/chat/" + encodedTeam);
            try { // Esegue: try {
                String payload = objectMapper.writeValueAsString(new OutgoingMessage(sender, content)); // Esegue: String payload = objectMapper.writeValueAsString(new OutgoingMessage(sender, content));
                HttpRequest.Builder builder = HttpRequest.newBuilder(uri) // Esegue: HttpRequest.Builder builder = HttpRequest.newBuilder(uri)
                        .header("Accept", "application/json") // Esegue: .header("Accept", "application/json")
                        .header("Content-Type", "application/json"); // Esegue: .header("Content-Type", "application/json");
                applyAuthorization(builder);
                HttpRequest request = builder
                        .POST(HttpRequest.BodyPublishers.ofString(payload)) // Esegue: .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .build(); // Esegue: .build();
                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString()) // Esegue: return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenCompose(response -> { // Esegue: .thenCompose(response -> {
                            if (isSuccess(response.statusCode())) { // Esegue: if (isSuccess(response.statusCode())) {
                                try { // Esegue: try {
                                    ChatMessage message = objectMapper.readValue(response.body(), ChatMessage.class); // Esegue: ChatMessage message = objectMapper.readValue(response.body(), ChatMessage.class);
                                    return CompletableFuture.completedFuture(message); // Esegue: return CompletableFuture.completedFuture(message);
                                } catch (Exception e) { // Esegue: } catch (Exception e) {
                                    return CompletableFuture.failedFuture(e); // Esegue: return CompletableFuture.failedFuture(e);
                                } // Esegue: }
                            } // Esegue: }
                            return CompletableFuture.failedFuture(new IllegalStateException( // Esegue: return CompletableFuture.failedFuture(new IllegalStateException(
                                    "Unexpected response status: " + response.statusCode())); // Esegue: "Unexpected response status: " + response.statusCode()));
                        }); // Esegue: });
            } catch (Exception e) { // Esegue: } catch (Exception e) {
                return CompletableFuture.failedFuture(e); // Esegue: return CompletableFuture.failedFuture(e);
            } // Esegue: }
        }); // Esegue: });
    } // Esegue: }

    public CompletableFuture<AutoCloseable> subscribeToTeam(String teamName, // Esegue: public CompletableFuture<AutoCloseable> subscribeToTeam(String teamName,
                                                            Consumer<ChatMessage> onMessage, // Esegue: Consumer<ChatMessage> onMessage,
                                                            Consumer<Throwable> onError) { // Esegue: Consumer<Throwable> onError) {
        Objects.requireNonNull(onMessage, "onMessage"); // Esegue: Objects.requireNonNull(onMessage, "onMessage");
        Objects.requireNonNull(onError, "onError"); // Esegue: Objects.requireNonNull(onError, "onError");
        return taskScheduler.schedule(() -> { // Esegue: return taskScheduler.schedule(() -> {
            if (teamName == null || teamName.isBlank()) { // Esegue: if (teamName == null || teamName.isBlank()) {
                return CompletableFuture.failedFuture( // Esegue: return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Il nome del team non può essere vuoto")); // Esegue: new IllegalArgumentException("Il nome del team non può essere vuoto"));
            } // Esegue: }
            String trimmedTeam = teamName.trim(); // Esegue: String trimmedTeam = teamName.trim();
            String teamKey = normalizeKey(trimmedTeam); // Esegue: String teamKey = normalizeKey(trimmedTeam);
            disconnectFromTeam(trimmedTeam); // Esegue: disconnectFromTeam(trimmedTeam);
            URI webSocketUri; // Esegue: URI webSocketUri;
            try { // Esegue: try {
                webSocketUri = buildWebSocketUri(trimmedTeam); // Esegue: webSocketUri = buildWebSocketUri(trimmedTeam);
            } catch (URISyntaxException e) { // Esegue: } catch (URISyntaxException e) {
                return CompletableFuture.failedFuture(e); // Esegue: return CompletableFuture.failedFuture(e);
            } // Esegue: }
            TeamWebSocketListener listener = new TeamWebSocketListener(objectMapper, onMessage, onError); // Esegue: TeamWebSocketListener listener = new TeamWebSocketListener(objectMapper, onMessage, onError);
            java.net.http.WebSocket.Builder builder = httpClient.newWebSocketBuilder() // Esegue: java.net.http.WebSocket.Builder builder = httpClient.newWebSocketBuilder()
                    .connectTimeout(DEFAULT_TIMEOUT); // Esegue: .connectTimeout(DEFAULT_TIMEOUT);
            applyAuthorization(builder);
            return builder.buildAsync(webSocketUri, listener) // Esegue: builder.buildAsync(webSocketUri, listener)
                    .thenApply(webSocket -> { // Esegue: .thenApply(webSocket -> {
                        TeamConnection connection = new TeamConnection(webSocket); // Esegue: TeamConnection connection = new TeamConnection(webSocket);
                        activeConnections.put(teamKey, connection); // Esegue: activeConnections.put(teamKey, connection);
                        return connection.asCloseable(teamKey, activeConnections); // Esegue: return connection.asCloseable(teamKey, activeConnections);
                    }) // Esegue: })
                    .whenComplete((closer, error) -> { // Esegue: .whenComplete((closer, error) -> {
                        if (error != null) { // Esegue: if (error != null) {
                            Throwable cause = (error instanceof CompletionException && error.getCause() != null) // Esegue: Throwable cause = (error instanceof CompletionException && error.getCause() != null)
                                    ? error.getCause() : error; // Esegue: ? error.getCause() : error;
                            onError.accept(cause); // Esegue: onError.accept(cause);
                        } // Esegue: }
                    }); // Esegue: });
        }); // Esegue: });
    } // Esegue: }

    public void disconnectFromTeam(String teamName) { // Esegue: public void disconnectFromTeam(String teamName) {
        if (teamName == null || teamName.isBlank()) { // Esegue: if (teamName == null || teamName.isBlank()) {
            return; // Esegue: return;
        } // Esegue: }
        String teamKey = normalizeKey(teamName); // Esegue: String teamKey = normalizeKey(teamName);
        TeamConnection connection = activeConnections.remove(teamKey); // Esegue: TeamConnection connection = activeConnections.remove(teamKey);
        if (connection != null) { // Esegue: if (connection != null) {
            connection.closeSilently(); // Esegue: connection.closeSilently();
        } // Esegue: }
    } // Esegue: }

    public void disconnectAll() { // Esegue: public void disconnectAll() {
        activeConnections.values().forEach(TeamConnection::closeSilently); // Esegue: activeConnections.values().forEach(TeamConnection::closeSilently);
        activeConnections.clear(); // Esegue: activeConnections.clear();
    } // Esegue: }

    public void setAuthToken(String authToken) {
        this.authToken = authToken != null && !authToken.isBlank() ? authToken : null;
    }

    private boolean isSuccess(int statusCode) { // Esegue: private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300; // Esegue: return statusCode >= 200 && statusCode < 300;
    } // Esegue: }

    private void applyAuthorization(HttpRequest.Builder builder) {
        String token = authToken;
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }
    }

    private void applyAuthorization(java.net.http.WebSocket.Builder builder) {
        String token = authToken;
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }
    }

    private URI buildWebSocketUri(String teamName) throws URISyntaxException { // Esegue: private URI buildWebSocketUri(String teamName) throws URISyntaxException {
        String scheme = toWebSocketScheme(baseUri.getScheme()); // Esegue: String scheme = toWebSocketScheme(baseUri.getScheme());
        String path = baseUri.getPath(); // Esegue: String path = baseUri.getPath();
        if (path == null) { // Esegue: if (path == null) {
            path = ""; // Esegue: path = "";
        } // Esegue: }
        if (!path.endsWith("/")) { // Esegue: if (!path.endsWith("/")) {
            path = path + "/"; // Esegue: path = path + "/";
        } // Esegue: }
        String wsPath = path + "ws/chat"; // Esegue: String wsPath = path + "ws/chat";
        String query = "team=" + URLEncoder.encode(teamName, StandardCharsets.UTF_8); // Esegue: String query = "team=" + URLEncoder.encode(teamName, StandardCharsets.UTF_8);
        return new URI(scheme, baseUri.getUserInfo(), baseUri.getHost(), baseUri.getPort(), wsPath, query, null); // Esegue: return new URI(scheme, baseUri.getUserInfo(), baseUri.getHost(), baseUri.getPort(), wsPath, query, null);
    } // Esegue: }

    private String toWebSocketScheme(String scheme) { // Esegue: private String toWebSocketScheme(String scheme) {
        if (scheme == null) { // Esegue: if (scheme == null) {
            return "ws"; // Esegue: return "ws";
        } // Esegue: }
        return scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("wss") ? "wss" : "ws"; // Esegue: return scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("wss") ? "wss" : "ws";
    } // Esegue: }

    private String normalizeKey(String teamName) { // Esegue: private String normalizeKey(String teamName) {
        return teamName.trim().toLowerCase(Locale.ROOT); // Esegue: return teamName.trim().toLowerCase(Locale.ROOT);
    } // Esegue: }

    private static ObjectMapper defaultObjectMapper() { // Esegue: private static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper(); // Esegue: ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Esegue: mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Esegue: mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper; // Esegue: return mapper;
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
        return URI.create(baseUrl); // Esegue: return URI.create(baseUrl);
    } // Esegue: }

    private static URI normalizeBaseUri(URI baseUri) { // Esegue: private static URI normalizeBaseUri(URI baseUri) {
        String baseUrl = baseUri.toString(); // Esegue: String baseUrl = baseUri.toString();
        if (!baseUrl.endsWith("/")) { // Esegue: if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/"; // Esegue: baseUrl = baseUrl + "/";
        } // Esegue: }
        return URI.create(baseUrl); // Esegue: return URI.create(baseUrl);
    } // Esegue: }

    private record OutgoingMessage(String sender, String content) { // Esegue: private record OutgoingMessage(String sender, String content) {
    } // Esegue: }

    private static final class TeamConnection { // Esegue: private static final class TeamConnection {
        private final WebSocket webSocket; // Esegue: private final WebSocket webSocket;

        private TeamConnection(WebSocket webSocket) { // Esegue: private TeamConnection(WebSocket webSocket) {
            this.webSocket = webSocket; // Esegue: this.webSocket = webSocket;
        } // Esegue: }

        private AutoCloseable asCloseable(String teamKey, Map<String, TeamConnection> registry) { // Esegue: private AutoCloseable asCloseable(String teamKey, Map<String, TeamConnection> registry) {
            return () -> { // Esegue: return () -> {
                registry.remove(teamKey); // Esegue: registry.remove(teamKey);
                closeSilently(); // Esegue: closeSilently();
            }; // Esegue: };
        } // Esegue: }

        private void closeSilently() { // Esegue: private void closeSilently() {
            try { // Esegue: try {
                webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Client closing"); // Esegue: webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Client closing");
            } catch (Exception ignored) { // Esegue: } catch (Exception ignored) {
                // ignored
            } finally { // Esegue: } finally {
                webSocket.abort(); // Esegue: webSocket.abort();
            } // Esegue: }
        } // Esegue: }
    } // Esegue: }

    private static final class TeamWebSocketListener implements WebSocket.Listener { // Esegue: private static final class TeamWebSocketListener implements WebSocket.Listener {
        private final ObjectMapper objectMapper; // Esegue: private final ObjectMapper objectMapper;
        private final Consumer<ChatMessage> onMessage; // Esegue: private final Consumer<ChatMessage> onMessage;
        private final Consumer<Throwable> onError; // Esegue: private final Consumer<Throwable> onError;
        private final StringBuilder buffer = new StringBuilder(); // Esegue: private final StringBuilder buffer = new StringBuilder();

        private TeamWebSocketListener(ObjectMapper objectMapper, // Esegue: private TeamWebSocketListener(ObjectMapper objectMapper,
                                      Consumer<ChatMessage> onMessage, // Esegue: Consumer<ChatMessage> onMessage,
                                      Consumer<Throwable> onError) { // Esegue: Consumer<Throwable> onError) {
            this.objectMapper = objectMapper; // Esegue: this.objectMapper = objectMapper;
            this.onMessage = onMessage; // Esegue: this.onMessage = onMessage;
            this.onError = onError; // Esegue: this.onError = onError;
        } // Esegue: }

        @Override // Esegue: @Override
        public void onOpen(WebSocket webSocket) { // Esegue: public void onOpen(WebSocket webSocket) {
            WebSocket.Listener.super.onOpen(webSocket); // Esegue: WebSocket.Listener.super.onOpen(webSocket);
            webSocket.request(1); // Esegue: webSocket.request(1);
        } // Esegue: }

        @Override // Esegue: @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) { // Esegue: public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            buffer.append(data); // Esegue: buffer.append(data);
            if (last) { // Esegue: if (last) {
                try { // Esegue: try {
                    ChatMessage message = objectMapper.readValue(buffer.toString(), ChatMessage.class); // Esegue: ChatMessage message = objectMapper.readValue(buffer.toString(), ChatMessage.class);
                    onMessage.accept(message); // Esegue: onMessage.accept(message);
                } catch (Exception e) { // Esegue: } catch (Exception e) {
                    onError.accept(e); // Esegue: onError.accept(e);
                } finally { // Esegue: } finally {
                    buffer.setLength(0); // Esegue: buffer.setLength(0);
                } // Esegue: }
            } // Esegue: }
            webSocket.request(1); // Esegue: webSocket.request(1);
            return null; // Esegue: return null;
        } // Esegue: }

        @Override // Esegue: @Override
        public void onError(WebSocket webSocket, Throwable error) { // Esegue: public void onError(WebSocket webSocket, Throwable error) {
            onError.accept(error); // Esegue: onError.accept(error);
        } // Esegue: }

        @Override // Esegue: @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) { // Esegue: public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason); // Esegue: return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        } // Esegue: }
    } // Esegue: }
} // Esegue: }
