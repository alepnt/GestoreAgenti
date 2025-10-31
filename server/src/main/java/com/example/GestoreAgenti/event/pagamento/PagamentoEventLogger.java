package com.example.GestoreAgenti.event.pagamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.GestoreAgenti.event.DomainEventPublisher;

@Component
public class PagamentoEventLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoEventLogger.class);

    public PagamentoEventLogger(DomainEventPublisher publisher) {
        publisher.subscribe(PagamentoStatusChangedEvent.class, this::logStatusChange);
        publisher.subscribe(PagamentoCreatedEvent.class, event ->
                LOGGER.info("Creato pagamento {} in stato {}", event.pagamento().getIdPagamento(), event.pagamento().getStato()));
        publisher.subscribe(PagamentoDeletedEvent.class, event ->
                LOGGER.info("Eliminato pagamento {}", event.pagamento().getIdPagamento()));
    }

    private void logStatusChange(PagamentoStatusChangedEvent event) {
        LOGGER.info("Pagamento {} passato da {} a {}", event.pagamento().getIdPagamento(),
                event.previousStatus(), event.newStatus());
    }
}
