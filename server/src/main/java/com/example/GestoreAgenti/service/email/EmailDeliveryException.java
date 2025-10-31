package com.example.GestoreAgenti.service.email;

/**
 * Eccezione generica lanciata quando l'invio dell'email non va a buon fine.
 */
public class EmailDeliveryException extends RuntimeException {

    public EmailDeliveryException(String message) {
        super(message);
    }

    public EmailDeliveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
