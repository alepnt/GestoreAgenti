package com.example.GestoreAgenti.fx.event;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class FxEventBusTest {

    private final FxEventBus eventBus = new FxEventBus();

    @Test
    void subscriberReceivesPublishedEvent() {
        AtomicInteger counter = new AtomicInteger();
        AutoCloseable subscription = eventBus.subscribe(SimpleEvent.class, event -> counter.incrementAndGet());

        eventBus.publish(new SimpleEvent());
        assertEquals(1, counter.get());

        assertDoesNotThrow(() -> subscription.close());
        eventBus.publish(new SimpleEvent());
        assertEquals(1, counter.get(), "Listener should be removed after close");
    }

    @Test
    void dispatchResolvesInheritanceAndInterfaces() {
        AtomicInteger baseCounter = new AtomicInteger();
        AtomicInteger markerCounter = new AtomicInteger();
        AtomicInteger concreteCounter = new AtomicInteger();

        eventBus.subscribe(BaseEvent.class, event -> baseCounter.incrementAndGet());
        eventBus.subscribe(MarkerEvent.class, event -> markerCounter.incrementAndGet());
        eventBus.subscribe(ConcreteEvent.class, event -> concreteCounter.incrementAndGet());

        eventBus.publish(new ConcreteEvent());

        assertEquals(1, baseCounter.get(), "Base type listener should receive the event");
        assertEquals(1, markerCounter.get(), "Interface listener should receive the event");
        assertEquals(1, concreteCounter.get(), "Concrete type listener should receive the event");
    }

    @Test
    void publishRejectsNullEvents() {
        assertThrows(NullPointerException.class, () -> eventBus.publish(null));
    }

    @Test
    void subscribeRejectsNullArguments() {
        assertThrows(NullPointerException.class, () -> eventBus.subscribe(null, event -> {}));
        assertThrows(NullPointerException.class, () -> eventBus.subscribe(SimpleEvent.class, null));
    }

    private static class SimpleEvent implements FxEvent {}

    private interface MarkerEvent extends FxEvent {}

    private static class BaseEvent implements FxEvent {}

    private static class ConcreteEvent extends BaseEvent implements MarkerEvent {}
}
