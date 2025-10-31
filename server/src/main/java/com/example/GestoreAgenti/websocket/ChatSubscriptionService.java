package com.example.GestoreAgenti.websocket;

import com.example.GestoreAgenti.model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
public class ChatSubscriptionService {

    private static final String TEAM_ATTRIBUTE = "team";

    private final Map<String, Set<WebSocketSession>> sessionsByTeam = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public ChatSubscriptionService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registerSession(String teamName, WebSocketSession session) {
        String normalizedTeam = normalizeTeam(teamName);
        session.getAttributes().put(TEAM_ATTRIBUTE, normalizedTeam);
        sessionsByTeam.computeIfAbsent(normalizedTeam, key -> new CopyOnWriteArraySet<>())
                .add(session);
    }

    public void unregisterSession(WebSocketSession session) {
        Object attribute = session.getAttributes().get(TEAM_ATTRIBUTE);
        if (!(attribute instanceof String teamKey)) {
            return;
        }
        Set<WebSocketSession> sessions = sessionsByTeam.get(teamKey);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                sessionsByTeam.remove(teamKey);
            }
        }
    }

    public void broadcast(ChatMessage message) {
        if (message == null) {
            return;
        }
        String normalizedTeam = normalizeTeam(message.teamName());
        Set<WebSocketSession> sessions = sessionsByTeam.get(normalizedTeam);
        if (sessions == null || sessions.isEmpty()) {
            return;
        }
        TextMessage payload = toTextMessage(message);
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) {
                unregisterSession(session);
                continue;
            }
            try {
                session.sendMessage(payload);
            } catch (IOException e) {
                unregisterSession(session);
            }
        }
    }

    private TextMessage toTextMessage(ChatMessage message) {
        try {
            return new TextMessage(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Impossibile serializzare il messaggio di chat", e);
        }
    }

    private String normalizeTeam(String teamName) {
        return teamName == null ? "" : teamName.trim().toLowerCase();
    }
}
