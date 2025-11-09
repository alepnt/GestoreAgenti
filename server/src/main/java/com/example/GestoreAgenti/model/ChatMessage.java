package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model che contiene questa classe.

import java.time.LocalDateTime; // Importa java.time.LocalDateTime per abilitare le funzionalità utilizzate nel file.

/**
 * Rappresenta un messaggio inviato nella chat di un team.
 */
public record ChatMessage(String teamName, String sender, LocalDateTime timestamp, String content) { // Definisce la record ChatMessage che incapsula la logica applicativa.
} // Chiude il blocco di codice precedente.
