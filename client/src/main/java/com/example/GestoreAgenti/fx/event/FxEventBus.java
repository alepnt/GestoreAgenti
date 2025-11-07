package com.example.GestoreAgenti.fx.event; // Esegue: package com.example.GestoreAgenti.fx.event;

import java.util.LinkedHashSet; // Esegue: import java.util.LinkedHashSet;
import java.util.List; // Esegue: import java.util.List;
import java.util.Map; // Esegue: import java.util.Map;
import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap; // Esegue: import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList; // Esegue: import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer; // Esegue: import java.util.function.Consumer;

/**
 * Simple publish/subscribe event bus to decouple controllers and services in
 * the JavaFX client.
 */
public class FxEventBus { // Esegue: public class FxEventBus {

    private final Map<Class<?>, CopyOnWriteArrayList<Consumer<? super FxEvent>>> listeners = new ConcurrentHashMap<>(); // Esegue: private final Map<Class<?>, CopyOnWriteArrayList<Consumer<? super FxEvent>>> listeners = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Class<?>>> dispatchCache = new ConcurrentHashMap<>(); // Esegue: private final Map<Class<?>, List<Class<?>>> dispatchCache = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked") // Esegue: @SuppressWarnings("unchecked")
    public <E extends FxEvent> AutoCloseable subscribe(Class<E> eventType, Consumer<E> listener) { // Esegue: public <E extends FxEvent> AutoCloseable subscribe(Class<E> eventType, Consumer<E> listener) {
        Objects.requireNonNull(eventType, "eventType"); // Esegue: Objects.requireNonNull(eventType, "eventType");
        Objects.requireNonNull(listener, "listener"); // Esegue: Objects.requireNonNull(listener, "listener");
        Consumer<? super FxEvent> bridge = (Consumer<? super FxEvent>) listener; // Esegue: Consumer<? super FxEvent> bridge = (Consumer<? super FxEvent>) listener;
        listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(bridge); // Esegue: listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(bridge);
        return () -> removeListener(eventType, bridge); // Esegue: return () -> removeListener(eventType, bridge);
    } // Esegue: }

    public void publish(FxEvent event) { // Esegue: public void publish(FxEvent event) {
        Objects.requireNonNull(event, "event"); // Esegue: Objects.requireNonNull(event, "event");
        List<Class<?>> targets = dispatchCache.computeIfAbsent(event.getClass(), this::resolveDispatchTargets); // Esegue: List<Class<?>> targets = dispatchCache.computeIfAbsent(event.getClass(), this::resolveDispatchTargets);
        for (Class<?> target : targets) { // Esegue: for (Class<?> target : targets) {
            CopyOnWriteArrayList<Consumer<? super FxEvent>> consumers = listeners.get(target); // Esegue: CopyOnWriteArrayList<Consumer<? super FxEvent>> consumers = listeners.get(target);
            if (consumers == null) { // Esegue: if (consumers == null) {
                continue; // Esegue: continue;
            } // Esegue: }
            for (Consumer<? super FxEvent> consumer : consumers) { // Esegue: for (Consumer<? super FxEvent> consumer : consumers) {
                @SuppressWarnings("unchecked") // Esegue: @SuppressWarnings("unchecked")
                Consumer<FxEvent> casted = (Consumer<FxEvent>) consumer; // Esegue: Consumer<FxEvent> casted = (Consumer<FxEvent>) consumer;
                casted.accept(event); // Esegue: casted.accept(event);
            } // Esegue: }
        } // Esegue: }
    } // Esegue: }

    private void removeListener(Class<?> eventType, Consumer<? super FxEvent> listener) { // Esegue: private void removeListener(Class<?> eventType, Consumer<? super FxEvent> listener) {
        listeners.computeIfPresent(eventType, (type, consumers) -> { // Esegue: listeners.computeIfPresent(eventType, (type, consumers) -> {
            consumers.remove(listener); // Esegue: consumers.remove(listener);
            return consumers.isEmpty() ? null : consumers; // Esegue: return consumers.isEmpty() ? null : consumers;
        }); // Esegue: });
    } // Esegue: }

    private List<Class<?>> resolveDispatchTargets(Class<?> eventClass) { // Esegue: private List<Class<?>> resolveDispatchTargets(Class<?> eventClass) {
        LinkedHashSet<Class<?>> result = new LinkedHashSet<>(); // Esegue: LinkedHashSet<Class<?>> result = new LinkedHashSet<>();
        Class<?> current = eventClass; // Esegue: Class<?> current = eventClass;
        while (current != null && current != Object.class) { // Esegue: while (current != null && current != Object.class) {
            result.add(current); // Esegue: result.add(current);
            collectInterfaces(current, result); // Esegue: collectInterfaces(current, result);
            current = current.getSuperclass(); // Esegue: current = current.getSuperclass();
        } // Esegue: }
        return List.copyOf(result); // Esegue: return List.copyOf(result);
    } // Esegue: }

    private void collectInterfaces(Class<?> type, LinkedHashSet<Class<?>> accumulator) { // Esegue: private void collectInterfaces(Class<?> type, LinkedHashSet<Class<?>> accumulator) {
        if (type == null) { // Esegue: if (type == null) {
            return; // Esegue: return;
        } // Esegue: }
        for (Class<?> iface : type.getInterfaces()) { // Esegue: for (Class<?> iface : type.getInterfaces()) {
            if (accumulator.add(iface)) { // Esegue: if (accumulator.add(iface)) {
                collectInterfaces(iface, accumulator); // Esegue: collectInterfaces(iface, accumulator);
            } // Esegue: }
        } // Esegue: }
    } // Esegue: }
} // Esegue: }
