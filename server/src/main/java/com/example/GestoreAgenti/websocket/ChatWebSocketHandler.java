package com.example.GestoreAgenti.websocket;

import com.example.GestoreAgenti.model.ChatMessage;
import com.example.GestoreAgenti.model.ChatMessageRequest;
import com.example.GestoreAgenti.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;
    private final ChatSubscriptionService subscriptionService;
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(ChatService chatService,
                                ChatSubscriptionService subscriptionService,
                                ObjectMapper objectMapper) {
        this.chatService = chatService;
        this.subscriptionService = subscriptionService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String teamName = resolveTeamName(session.getUri());
        if (teamName == null || teamName.isBlank()) {
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        subscriptionService.registerSession(teamName, session);
        sendHistory(session, teamName);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessageRequest request = objectMapper.readValue(message.getPayload(), ChatMessageRequest.class);
        if (request.content() == null || request.content().isBlank()
                || request.sender() == null || request.sender().isBlank()) {
            return;
        }
        String teamName = resolveTeamName(session.getUri());
        if (teamName == null || teamName.isBlank()) {
            return;
        }
        ChatMessage chatMessage = chatService.appendMessage(teamName, request.sender(), request.content());
        subscriptionService.broadcast(chatMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        subscriptionService.unregisterSession(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        subscriptionService.unregisterSession(session);
        session.close(CloseStatus.SERVER_ERROR);
    }

    private void sendHistory(WebSocketSession session, String teamName) throws IOException {
        List<ChatMessage> history = chatService.getMessagesForTeam(teamName);
        for (ChatMessage chatMessage : history) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
        }
    }

    private String resolveTeamName(URI uri) {
        if (uri == null) {
            return null;
        }
        String query = uri.getQuery();
        if (query == null || query.isBlank()) {
            return null;
        }
        for (String parameter : query.split("&")) {
            String[] keyValue = parameter.split("=", 2);
            if (keyValue.length == 2 && keyValue[0].equals("team")) {
                return URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8).trim();
            }
        }
        return null;
    }
}
