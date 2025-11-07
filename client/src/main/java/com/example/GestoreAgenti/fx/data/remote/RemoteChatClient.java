package com.example.GestoreAgenti.fx.data.remote;

import com.example.GestoreAgenti.fx.model.ChatMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Client responsabile di sincronizzare i messaggi di chat con il server Spring Boot.
 */
public class RemoteChatClient {

    private static final TypeReference<List<ChatMessage>> CHAT_LIST_TYPE = new TypeReference<>() { };
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(5);

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final URI baseUri;
    private final Map<String, TeamConnection> activeConnections = new ConcurrentHashMap<>();
    private final RemoteTaskScheduler taskScheduler;

    public RemoteChatClient(RemoteTaskScheduler taskScheduler) {
        this(taskScheduler,
                HttpClient.newBuilder()
                        .connectTimeout(DEFAULT_TIMEOUT)
                        .build(),
                defaultObjectMapper(),
                resolveBaseUri());
    }

    public RemoteChatClient() {
        this(new RemoteTaskScheduler(Math.max(1, Runtime.getRuntime().availableProcessors())));
    }

    RemoteChatClient(RemoteTaskScheduler taskScheduler,
                     HttpClient httpClient,
                     ObjectMapper objectMapper,
                     URI baseUri) {
        this.taskScheduler = Objects.requireNonNull(taskScheduler, "taskScheduler");
        this.httpClient = Objects.requireNonNull(httpClient, "httpClient");
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper");
        this.baseUri = normalizeBaseUri(Objects.requireNonNull(baseUri, "baseUri"));
    }

    public CompletableFuture<List<ChatMessage>> fetchTeamMessages(String teamName) {
        return taskScheduler.schedule(() -> {
            if (teamName == null || teamName.isBlank()) {
                return CompletableFuture.completedFuture(List.of());
            }
            String encodedTeam = URLEncoder.encode(teamName, StandardCharsets.UTF_8);
            URI uri = baseUri.resolve("api/chat/" + encodedTeam);
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .header("Accept", "application/json")
                    .GET()
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenCompose(response -> {
                        if (isSuccess(response.statusCode())) {
                            try {
                                List<ChatMessage> messages = objectMapper.readValue(response.body(), CHAT_LIST_TYPE);
                                return CompletableFuture.completedFuture(messages);
                            } catch (Exception e) {
                                return CompletableFuture.failedFuture(e);
                            }
                        }
                        return CompletableFuture.failedFuture(new IllegalStateException(
                                "Unexpected response status: " + response.statusCode()));
                    });
        });
    }

    public CompletableFuture<ChatMessage> sendTeamMessage(String teamName, String sender, String content) {
        return taskScheduler.schedule(() -> {
            if (teamName == null || teamName.isBlank()
                    || sender == null || sender.isBlank()
                    || content == null || content.isBlank()) {
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Team, mittente e contenuto devono essere valorizzati"));
            }
            String encodedTeam = URLEncoder.encode(teamName, StandardCharsets.UTF_8);
            URI uri = baseUri.resolve("api/chat/" + encodedTeam);
            try {
                String payload = objectMapper.writeValueAsString(new OutgoingMessage(sender, content));
                HttpRequest request = HttpRequest.newBuilder(uri)
                        .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .build();
                return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                        .thenCompose(response -> {
                            if (isSuccess(response.statusCode())) {
                                try {
                                    ChatMessage message = objectMapper.readValue(response.body(), ChatMessage.class);
                                    return CompletableFuture.completedFuture(message);
                                } catch (Exception e) {
                                    return CompletableFuture.failedFuture(e);
                                }
                            }
                            return CompletableFuture.failedFuture(new IllegalStateException(
                                    "Unexpected response status: " + response.statusCode()));
                        });
            } catch (Exception e) {
                return CompletableFuture.failedFuture(e);
            }
        });
    }

    public CompletableFuture<AutoCloseable> subscribeToTeam(String teamName,
                                                            Consumer<ChatMessage> onMessage,
                                                            Consumer<Throwable> onError) {
        Objects.requireNonNull(onMessage, "onMessage");
        Objects.requireNonNull(onError, "onError");
        return taskScheduler.schedule(() -> {
            if (teamName == null || teamName.isBlank()) {
                return CompletableFuture.failedFuture(
                        new IllegalArgumentException("Il nome del team non può essere vuoto"));
            }
            String trimmedTeam = teamName.trim();
            String teamKey = normalizeKey(trimmedTeam);
            disconnectFromTeam(trimmedTeam);
            URI webSocketUri;
            try {
                webSocketUri = buildWebSocketUri(trimmedTeam);
            } catch (URISyntaxException e) {
                return CompletableFuture.failedFuture(e);
            }
            TeamWebSocketListener listener = new TeamWebSocketListener(objectMapper, onMessage, onError);
            return httpClient.newWebSocketBuilder()
                    .connectTimeout(DEFAULT_TIMEOUT)
                    .buildAsync(webSocketUri, listener)
                    .thenApply(webSocket -> {
                        TeamConnection connection = new TeamConnection(webSocket);
                        activeConnections.put(teamKey, connection);
                        return connection.asCloseable(teamKey, activeConnections);
                    })
                    .whenComplete((closer, error) -> {
                        if (error != null) {
                            Throwable cause = (error instanceof CompletionException && error.getCause() != null)
                                    ? error.getCause() : error;
                            onError.accept(cause);
                        }
                    });
        });
    }

    public void disconnectFromTeam(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return;
        }
        String teamKey = normalizeKey(teamName);
        TeamConnection connection = activeConnections.remove(teamKey);
        if (connection != null) {
            connection.closeSilently();
        }
    }

    public void disconnectAll() {
        activeConnections.values().forEach(TeamConnection::closeSilently);
        activeConnections.clear();
    }

    private boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    private URI buildWebSocketUri(String teamName) throws URISyntaxException {
        String scheme = toWebSocketScheme(baseUri.getScheme());
        String path = baseUri.getPath();
        if (path == null) {
            path = "";
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        String wsPath = path + "ws/chat";
        String query = "team=" + URLEncoder.encode(teamName, StandardCharsets.UTF_8);
        return new URI(scheme, baseUri.getUserInfo(), baseUri.getHost(), baseUri.getPort(), wsPath, query, null);
    }

    private String toWebSocketScheme(String scheme) {
        if (scheme == null) {
            return "ws";
        }
        return scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("wss") ? "wss" : "ws";
    }

    private String normalizeKey(String teamName) {
        return teamName.trim().toLowerCase(Locale.ROOT);
    }

    private static ObjectMapper defaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
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
        return URI.create(baseUrl);
    }

    private static URI normalizeBaseUri(URI baseUri) {
        String baseUrl = baseUri.toString();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        return URI.create(baseUrl);
    }

    private record OutgoingMessage(String sender, String content) {
    }

    private static final class TeamConnection {
        private final WebSocket webSocket;

        private TeamConnection(WebSocket webSocket) {
            this.webSocket = webSocket;
        }

        private AutoCloseable asCloseable(String teamKey, Map<String, TeamConnection> registry) {
            return () -> {
                registry.remove(teamKey);
                closeSilently();
            };
        }

        private void closeSilently() {
            try {
                webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Client closing");
            } catch (Exception ignored) {
                // ignored
            } finally {
                webSocket.abort();
            }
        }
    }

    private static final class TeamWebSocketListener implements WebSocket.Listener {
        private final ObjectMapper objectMapper;
        private final Consumer<ChatMessage> onMessage;
        private final Consumer<Throwable> onError;
        private final StringBuilder buffer = new StringBuilder();

        private TeamWebSocketListener(ObjectMapper objectMapper,
                                      Consumer<ChatMessage> onMessage,
                                      Consumer<Throwable> onError) {
            this.objectMapper = objectMapper;
            this.onMessage = onMessage;
            this.onError = onError;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            WebSocket.Listener.super.onOpen(webSocket);
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            buffer.append(data);
            if (last) {
                try {
                    ChatMessage message = objectMapper.readValue(buffer.toString(), ChatMessage.class);
                    onMessage.accept(message);
                } catch (Exception e) {
                    onError.accept(e);
                } finally {
                    buffer.setLength(0);
                }
            }
            webSocket.request(1);
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            onError.accept(error);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
        }
    }
}
