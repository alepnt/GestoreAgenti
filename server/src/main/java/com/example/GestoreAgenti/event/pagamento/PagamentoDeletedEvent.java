package com.example.GestoreAgenti.event.pagamento;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;
import com.example.GestoreAgenti.model.Pagamento;

public record PagamentoDeletedEvent(Pagamento pagamento, Instant occurredOn) implements DomainEvent {

    public PagamentoDeletedEvent(Pagamento pagamento) {
        this(pagamento, Instant.now());
    }
}
