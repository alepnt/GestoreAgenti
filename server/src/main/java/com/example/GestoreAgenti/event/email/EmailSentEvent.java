package com.example.GestoreAgenti.event.email;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;

public record EmailSentEvent(String from,
                             String to,
                             String subject,
                             Instant occurredOn) implements DomainEvent {

    public EmailSentEvent(String from, String to, String subject) {
        this(from, to, subject, Instant.now());
    }
}
