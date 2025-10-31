package com.example.GestoreAgenti.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Rappresenta la richiesta JSON per inviare un messaggio email reale.
 */
public record EmailSendRequest(
        @NotBlank @Email String from,
        @NotBlank @Email String to,
        @NotBlank String subject,
        @NotBlank String body) {
}
