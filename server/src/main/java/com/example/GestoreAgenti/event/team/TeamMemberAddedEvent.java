package com.example.GestoreAgenti.event.team;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;
import com.example.GestoreAgenti.model.Dipendente;

public record TeamMemberAddedEvent(Dipendente member, Instant occurredOn) implements DomainEvent {

    public TeamMemberAddedEvent(Dipendente member) {
        this(member, Instant.now());
    }
}
