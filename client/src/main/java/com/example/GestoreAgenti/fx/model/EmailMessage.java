package com.example.GestoreAgenti.fx.model;

import java.time.LocalDateTime;

/**
 * Rappresenta una comunicazione e-mail scambiata con un cliente.
 */
public record EmailMessage(String sender, String recipient, String subject, String body,
                           LocalDateTime timestamp, boolean incoming) {
}
