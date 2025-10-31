package com.example.GestoreAgenti.model;

/**
 * Payload ricevuto quando un utente invia un nuovo messaggio nella chat di team.
 */
public record ChatMessageRequest(String sender, String content) {
}
