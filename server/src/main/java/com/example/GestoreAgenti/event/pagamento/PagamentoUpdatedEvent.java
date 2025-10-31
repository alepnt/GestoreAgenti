package com.example.GestoreAgenti.event.pagamento;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;
import com.example.GestoreAgenti.model.Pagamento;

public record PagamentoUpdatedEvent(Pagamento pagamento, Instant occurredOn) implements DomainEvent {

    public PagamentoUpdatedEvent(Pagamento pagamento) {
        this(pagamento, Instant.now());
    }
}
