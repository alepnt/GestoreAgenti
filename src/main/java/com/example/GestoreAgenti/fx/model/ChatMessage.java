package com.example.GestoreAgenti.fx.model;

import java.time.LocalDateTime;

/**
 * Messaggio condiviso in una chat di team.
 */
public record ChatMessage(String teamName, String sender, LocalDateTime timestamp, String content) {
}
