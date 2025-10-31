package com.example.GestoreAgenti.event.chat;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;
import com.example.GestoreAgenti.model.ChatMessage;

public record ChatMessageCreatedEvent(ChatMessage message, Instant occurredOn) implements DomainEvent {

    public ChatMessageCreatedEvent(ChatMessage message) {
        this(message, Instant.now());
    }
}
