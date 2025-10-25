package com.example.GestoreAgenti.fx.model;

import java.time.LocalDateTime;

/**
 * Evento presente nell'agenda del dipendente.
 */
public record AgendaItem(LocalDateTime start, LocalDateTime end, String description, String location) {
}
