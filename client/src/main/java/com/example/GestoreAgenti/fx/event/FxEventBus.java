package com.example.GestoreAgenti.fx.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Simple publish/subscribe event bus to decouple controllers and services in
 * the JavaFX client.
 */
public class FxEventBus {

    private final Map<Class<?>, List<Consumer<? super FxEvent>>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <E extends FxEvent> AutoCloseable subscribe(Class<E> eventType, Consumer<E> listener) {
        listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>())
                .add((Consumer<? super FxEvent>) listener);
        return () -> listeners.getOrDefault(eventType, List.of()).remove(listener);
    }

    public void publish(FxEvent event) {
        Class<?> eventClass = event.getClass();
        listeners.forEach((type, consumers) -> {
            if (type.isAssignableFrom(eventClass)) {
                for (Consumer<? super FxEvent> consumer : consumers) {
                    @SuppressWarnings("unchecked")
                    Consumer<FxEvent> casted = (Consumer<FxEvent>) consumer;
                    casted.accept(event);
                }
            }
        });
    }
}
