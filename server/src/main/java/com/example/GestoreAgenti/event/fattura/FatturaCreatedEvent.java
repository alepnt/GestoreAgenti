package com.example.GestoreAgenti.event.fattura;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;
import com.example.GestoreAgenti.model.Fattura;

public record FatturaCreatedEvent(Fattura fattura, Instant occurredOn) implements DomainEvent {

    public FatturaCreatedEvent(Fattura fattura) {
        this(fattura, Instant.now());
    }
}
