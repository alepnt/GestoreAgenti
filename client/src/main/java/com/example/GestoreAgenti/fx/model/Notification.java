package com.example.GestoreAgenti.fx.model; // Esegue: package com.example.GestoreAgenti.fx.model;

import java.time.LocalDateTime; // Esegue: import java.time.LocalDateTime;

/**
 * Notifica mostrata nell'applicazione.
 */
public record Notification(String title, String message, LocalDateTime createdAt, boolean read) { // Esegue: public record Notification(String title, String message, LocalDateTime createdAt, boolean read) {
} // Esegue: }
