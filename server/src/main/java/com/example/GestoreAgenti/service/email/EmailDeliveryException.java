package com.example.GestoreAgenti.service.email; // Definisce il pacchetto com.example.GestoreAgenti.service.email che contiene questa classe.

/**
 * Eccezione generica lanciata quando l'invio dell'email non va a buon fine.
 */
public class EmailDeliveryException extends RuntimeException { // Definisce la classe EmailDeliveryException che incapsula la logica applicativa.

    public EmailDeliveryException(String message) { // Costruttore della classe EmailDeliveryException che inizializza le dipendenze necessarie.
        super(message); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.

    public EmailDeliveryException(String message, Throwable cause) { // Costruttore della classe EmailDeliveryException che inizializza le dipendenze necessarie.
        super(message, cause); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
