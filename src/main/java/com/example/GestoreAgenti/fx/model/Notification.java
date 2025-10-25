package com.example.GestoreAgenti.fx.model;

import java.time.LocalDateTime;

/**
 * Notifica mostrata nell'applicazione.
 */
public record Notification(String title, String message, LocalDateTime createdAt, boolean read) {
}
