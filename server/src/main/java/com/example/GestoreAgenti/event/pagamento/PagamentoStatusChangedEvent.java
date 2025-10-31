package com.example.GestoreAgenti.event.pagamento;

import java.time.Instant;

import com.example.GestoreAgenti.event.DomainEvent;
import com.example.GestoreAgenti.model.Pagamento;

public record PagamentoStatusChangedEvent(Pagamento pagamento,
                                          String previousStatus,
                                          String newStatus,
                                          Instant occurredOn) implements DomainEvent {

    public PagamentoStatusChangedEvent(Pagamento pagamento, String previousStatus, String newStatus) {
        this(pagamento, previousStatus, newStatus, Instant.now());
    }
}
