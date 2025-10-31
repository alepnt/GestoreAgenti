package com.example.GestoreAgenti.event;

@FunctionalInterface
public interface DomainEventListener<E extends DomainEvent> {

    void onEvent(E event);
}
