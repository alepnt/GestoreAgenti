package com.example.GestoreAgenti.event.pagamento;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;
import com.example.GestoreAgenti.model.Pagamento;

public record PagamentoCreatedEvent(Pagamento pagamento, Instant occurredOn) implements DomainEvent {

    public PagamentoCreatedEvent(Pagamento pagamento) {
        this(pagamento, Instant.now());
    }
}
