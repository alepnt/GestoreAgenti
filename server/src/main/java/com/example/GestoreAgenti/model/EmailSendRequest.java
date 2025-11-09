package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model che contiene questa classe.

import jakarta.validation.constraints.Email; // Importa jakarta.validation.constraints.Email per abilitare le funzionalità utilizzate nel file.
import jakarta.validation.constraints.NotBlank; // Importa jakarta.validation.constraints.NotBlank per abilitare le funzionalità utilizzate nel file.

/**
 * Rappresenta la richiesta JSON per inviare un messaggio email reale.
 */
public record EmailSendRequest( // Definisce la record EmailSendRequest che incapsula la logica applicativa.
        @NotBlank @Email String from, // Applica l'annotazione @NotBlank @Email String from, per configurare il componente.
        @NotBlank @Email String to, // Applica l'annotazione @NotBlank @Email String to, per configurare il componente.
        @NotBlank String subject, // Applica l'annotazione @NotBlank String subject, per configurare il componente.
        @NotBlank String body) { // Applica l'annotazione @NotBlank String body) { per configurare il componente.
} // Chiude il blocco di codice precedente.
