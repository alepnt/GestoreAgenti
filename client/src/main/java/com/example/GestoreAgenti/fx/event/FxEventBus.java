package com.example.GestoreAgenti.fx.event;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Simple publish/subscribe event bus to decouple controllers and services in
 * the JavaFX client.
 */
public class FxEventBus {

    private final Map<Class<?>, CopyOnWriteArrayList<Consumer<? super FxEvent>>> listeners = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Class<?>>> dispatchCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <E extends FxEvent> AutoCloseable subscribe(Class<E> eventType, Consumer<E> listener) {
        Objects.requireNonNull(eventType, "eventType");
        Objects.requireNonNull(listener, "listener");
        Consumer<? super FxEvent> bridge = (Consumer<? super FxEvent>) listener;
        listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(bridge);
        return () -> removeListener(eventType, bridge);
    }

    public void publish(FxEvent event) {
        Objects.requireNonNull(event, "event");
        List<Class<?>> targets = dispatchCache.computeIfAbsent(event.getClass(), this::resolveDispatchTargets);
        for (Class<?> target : targets) {
            CopyOnWriteArrayList<Consumer<? super FxEvent>> consumers = listeners.get(target);
            if (consumers == null) {
                continue;
            }
            for (Consumer<? super FxEvent> consumer : consumers) {
                @SuppressWarnings("unchecked")
                Consumer<FxEvent> casted = (Consumer<FxEvent>) consumer;
                casted.accept(event);
            }
        }
    }

    private void removeListener(Class<?> eventType, Consumer<? super FxEvent> listener) {
        listeners.computeIfPresent(eventType, (type, consumers) -> {
            consumers.remove(listener);
            return consumers.isEmpty() ? null : consumers;
        });
    }

    private List<Class<?>> resolveDispatchTargets(Class<?> eventClass) {
        LinkedHashSet<Class<?>> result = new LinkedHashSet<>();
        Class<?> current = eventClass;
        while (current != null && current != Object.class) {
            result.add(current);
            collectInterfaces(current, result);
            current = current.getSuperclass();
        }
        return List.copyOf(result);
    }

    private void collectInterfaces(Class<?> type, LinkedHashSet<Class<?>> accumulator) {
        if (type == null) {
            return;
        }
        for (Class<?> iface : type.getInterfaces()) {
            if (accumulator.add(iface)) {
                collectInterfaces(iface, accumulator);
            }
        }
    }
}
