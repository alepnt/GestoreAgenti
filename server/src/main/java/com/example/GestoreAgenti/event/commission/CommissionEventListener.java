package com.example.GestoreAgenti.event.commission;

import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.example.GestoreAgenti.event.DomainEventPublisher;
import com.example.GestoreAgenti.event.fattura.FatturaCreatedEvent;
import com.example.GestoreAgenti.event.pagamento.PagamentoStatusChangedEvent;
import com.example.GestoreAgenti.service.CommissionCalculationService;

@Component
public class CommissionEventListener {

    private final DomainEventPublisher eventPublisher;
    private final CommissionCalculationService commissionCalculationService;
    private final List<AutoCloseable> subscriptions = new ArrayList<>();

    public CommissionEventListener(DomainEventPublisher eventPublisher,
                                   CommissionCalculationService commissionCalculationService) {
        this.eventPublisher = eventPublisher;
        this.commissionCalculationService = commissionCalculationService;
    }

    @PostConstruct
    void subscribe() {
        subscriptions.add(eventPublisher.subscribe(FatturaCreatedEvent.class, this::onFatturaCreated));
        subscriptions.add(eventPublisher.subscribe(PagamentoStatusChangedEvent.class, this::onPagamentoStatusChanged));
    }

    @PreDestroy
    void unsubscribe() {
        for (AutoCloseable subscription : subscriptions) {
            try {
                subscription.close();
            } catch (Exception ignored) {
                // intentionally ignored
            }
        }
        subscriptions.clear();
    }

    private void onFatturaCreated(FatturaCreatedEvent event) {
        commissionCalculationService.calcolaProvvigioni(event.fattura());
    }

    private void onPagamentoStatusChanged(PagamentoStatusChangedEvent event) {
        commissionCalculationService.consolidaProvvigioni(event.pagamento(), event.newStatus());
    }
}
