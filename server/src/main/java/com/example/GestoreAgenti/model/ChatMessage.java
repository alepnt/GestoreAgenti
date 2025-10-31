package com.example.GestoreAgenti.model;

import java.time.LocalDateTime;

/**
 * Rappresenta un messaggio inviato nella chat di un team.
 */
public record ChatMessage(String teamName, String sender, LocalDateTime timestamp, String content) {
}
