package com.example.GestoreAgenti.service.email; // Definisce il pacchetto com.example.GestoreAgenti.service.email che contiene questa classe.

/**
 * Segnala che l'invio delle email è stato disabilitato tramite configurazione.
 */
public class EmailDeliveryDisabledException extends EmailDeliveryException { // Definisce la classe EmailDeliveryDisabledException che incapsula la logica applicativa.

    public EmailDeliveryDisabledException(String message) { // Costruttore della classe EmailDeliveryDisabledException che inizializza le dipendenze necessarie.
        super(message); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
