package com.example.GestoreAgenti.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

/**
 * Simple in-memory event bus used to decouple the publishing service from the
 * listeners interested in reacting to domain events.
 */
@Component
public class DomainEventPublisher {

    private final Map<Class<?>, List<DomainEventListener<?>>> listeners = new ConcurrentHashMap<>();

    public <E extends DomainEvent> AutoCloseable subscribe(Class<E> eventType, DomainEventListener<E> listener) {
        listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(listener);
        return () -> listeners.getOrDefault(eventType, List.of()).remove(listener);
    }

    public void publish(DomainEvent event) {
        Class<?> eventClass = event.getClass();
        listeners.forEach((type, registeredListeners) -> {
            if (type.isAssignableFrom(eventClass)) {
                for (DomainEventListener<?> listener : registeredListeners) {
                    @SuppressWarnings("unchecked")
                    DomainEventListener<DomainEvent> typed = (DomainEventListener<DomainEvent>) listener;
                    typed.onEvent(event);
                }
            }
        });
    }
}
