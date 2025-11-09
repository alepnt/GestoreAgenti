package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model che contiene questa classe.

/**
 * Payload ricevuto quando un utente invia un nuovo messaggio nella chat di team.
 */
public record ChatMessageRequest(String sender, String content) { // Definisce la record ChatMessageRequest che incapsula la logica applicativa.
} // Chiude il blocco di codice precedente.
