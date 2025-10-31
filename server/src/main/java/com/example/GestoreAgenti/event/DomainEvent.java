package com.example.GestoreAgenti.event;

import java.time.Instant;

/**
 * Marker interface for domain events published by the backend.
 */
public interface DomainEvent {

    Instant occurredOn();
}
