package com.example.GestoreAgenti.service.email;

/**
 * Segnala che l'invio delle email è stato disabilitato tramite configurazione.
 */
public class EmailDeliveryDisabledException extends EmailDeliveryException {

    public EmailDeliveryDisabledException(String message) {
        super(message);
    }
}
