package com.example.GestoreAgenti.service;

import com.example.GestoreAgenti.event.DomainEventPublisher;
import com.example.GestoreAgenti.event.chat.ChatMessageCreatedEvent;
import com.example.GestoreAgenti.model.ChatMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gestisce la memorizzazione in memoria dei messaggi di chat.
 */
@Service
public class ChatService {

    private final Map<String, CopyOnWriteArrayList<ChatMessage>> messagesByTeam = new ConcurrentHashMap<>();
    private final DomainEventPublisher eventPublisher;

    public ChatService(DomainEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        seedDemoMessages();
    }

    public List<ChatMessage> getMessagesForTeam(String teamName) {
        if (teamName == null || teamName.isBlank()) {
            return List.of();
        }
        var normalizedKey = normalizeKey(teamName);
        var messages = messagesByTeam.get(normalizedKey);
        if (messages == null) {
            return List.of();
        }
        var sorted = new ArrayList<>(messages);
        sorted.sort(Comparator.comparing(ChatMessage::timestamp));
        return sorted;
    }

    public ChatMessage appendMessage(String teamName, String sender, String content) {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Il team non può essere vuoto");
        }
        if (sender == null || sender.isBlank()) {
            throw new IllegalArgumentException("Il mittente non può essere vuoto");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Il contenuto non può essere vuoto");
        }
        String trimmedTeam = teamName.trim();
        String trimmedSender = sender.trim();
        String trimmedContent = content.trim();
        ChatMessage message = new ChatMessage(trimmedTeam, trimmedSender, LocalDateTime.now(), trimmedContent);
        messagesByTeam.computeIfAbsent(normalizeKey(trimmedTeam), key -> new CopyOnWriteArrayList<>())
                .add(message);
        eventPublisher.publish(new ChatMessageCreatedEvent(message));
        return message;
    }

    private void seedDemoMessages() {
        LocalDateTime now = LocalDateTime.now();
        var nordMessages = messagesByTeam.computeIfAbsent(normalizeKey("Team Nord"), key -> new CopyOnWriteArrayList<>());
        nordMessages.add(new ChatMessage("Team Nord", "Mario Rossi", now.minusMinutes(50),
                "Ricordatevi il meeting di oggi alle 14"));
        nordMessages.add(new ChatMessage("Team Nord", "Lucia Bianchi", now.minusMinutes(45),
                "Perfetto, porterò il report clienti"));

        var centroMessages = messagesByTeam.computeIfAbsent(normalizeKey("Team Centro"), key -> new CopyOnWriteArrayList<>());
        centroMessages.add(new ChatMessage("Team Centro", "Giulia Verdi", now.minusMinutes(30),
                "Aggiornato il backlog del progetto Gamma"));
    }

    private String normalizeKey(String teamName) {
        return teamName.trim().toLowerCase(Locale.ITALIAN);
    }
}
