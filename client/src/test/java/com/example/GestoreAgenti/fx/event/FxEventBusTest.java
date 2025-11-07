package com.example.GestoreAgenti.fx.event; // Esegue: package com.example.GestoreAgenti.fx.event;

import org.junit.jupiter.api.Test; // Esegue: import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger; // Esegue: import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*; // Esegue: import static org.junit.jupiter.api.Assertions.*;

class FxEventBusTest { // Esegue: class FxEventBusTest {

    private final FxEventBus eventBus = new FxEventBus(); // Esegue: private final FxEventBus eventBus = new FxEventBus();

    @Test // Esegue: @Test
    void subscriberReceivesPublishedEvent() { // Esegue: void subscriberReceivesPublishedEvent() {
        AtomicInteger counter = new AtomicInteger(); // Esegue: AtomicInteger counter = new AtomicInteger();
        AutoCloseable subscription = eventBus.subscribe(SimpleEvent.class, event -> counter.incrementAndGet()); // Esegue: AutoCloseable subscription = eventBus.subscribe(SimpleEvent.class, event -> counter.incrementAndGet());

        eventBus.publish(new SimpleEvent()); // Esegue: eventBus.publish(new SimpleEvent());
        assertEquals(1, counter.get()); // Esegue: assertEquals(1, counter.get());

        assertDoesNotThrow(() -> subscription.close()); // Esegue: assertDoesNotThrow(() -> subscription.close());
        eventBus.publish(new SimpleEvent()); // Esegue: eventBus.publish(new SimpleEvent());
        assertEquals(1, counter.get(), "Listener should be removed after close"); // Esegue: assertEquals(1, counter.get(), "Listener should be removed after close");
    } // Esegue: }

    @Test // Esegue: @Test
    void dispatchResolvesInheritanceAndInterfaces() { // Esegue: void dispatchResolvesInheritanceAndInterfaces() {
        AtomicInteger baseCounter = new AtomicInteger(); // Esegue: AtomicInteger baseCounter = new AtomicInteger();
        AtomicInteger markerCounter = new AtomicInteger(); // Esegue: AtomicInteger markerCounter = new AtomicInteger();
        AtomicInteger concreteCounter = new AtomicInteger(); // Esegue: AtomicInteger concreteCounter = new AtomicInteger();

        eventBus.subscribe(BaseEvent.class, event -> baseCounter.incrementAndGet()); // Esegue: eventBus.subscribe(BaseEvent.class, event -> baseCounter.incrementAndGet());
        eventBus.subscribe(MarkerEvent.class, event -> markerCounter.incrementAndGet()); // Esegue: eventBus.subscribe(MarkerEvent.class, event -> markerCounter.incrementAndGet());
        eventBus.subscribe(ConcreteEvent.class, event -> concreteCounter.incrementAndGet()); // Esegue: eventBus.subscribe(ConcreteEvent.class, event -> concreteCounter.incrementAndGet());

        eventBus.publish(new ConcreteEvent()); // Esegue: eventBus.publish(new ConcreteEvent());

        assertEquals(1, baseCounter.get(), "Base type listener should receive the event"); // Esegue: assertEquals(1, baseCounter.get(), "Base type listener should receive the event");
        assertEquals(1, markerCounter.get(), "Interface listener should receive the event"); // Esegue: assertEquals(1, markerCounter.get(), "Interface listener should receive the event");
        assertEquals(1, concreteCounter.get(), "Concrete type listener should receive the event"); // Esegue: assertEquals(1, concreteCounter.get(), "Concrete type listener should receive the event");
    } // Esegue: }

    @Test // Esegue: @Test
    void publishRejectsNullEvents() { // Esegue: void publishRejectsNullEvents() {
        assertThrows(NullPointerException.class, () -> eventBus.publish(null)); // Esegue: assertThrows(NullPointerException.class, () -> eventBus.publish(null));
    } // Esegue: }

    @Test // Esegue: @Test
    void subscribeRejectsNullArguments() { // Esegue: void subscribeRejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> eventBus.subscribe(null, event -> {})); // Esegue: assertThrows(NullPointerException.class, () -> eventBus.subscribe(null, event -> {}));
        assertThrows(NullPointerException.class, () -> eventBus.subscribe(SimpleEvent.class, null)); // Esegue: assertThrows(NullPointerException.class, () -> eventBus.subscribe(SimpleEvent.class, null));
    } // Esegue: }

    private static class SimpleEvent implements FxEvent {} // Esegue: private static class SimpleEvent implements FxEvent {}

    private interface MarkerEvent extends FxEvent {} // Esegue: private interface MarkerEvent extends FxEvent {}

    private static class BaseEvent implements FxEvent {} // Esegue: private static class BaseEvent implements FxEvent {}

    private static class ConcreteEvent extends BaseEvent implements MarkerEvent {} // Esegue: private static class ConcreteEvent extends BaseEvent implements MarkerEvent {}
} // Esegue: }
