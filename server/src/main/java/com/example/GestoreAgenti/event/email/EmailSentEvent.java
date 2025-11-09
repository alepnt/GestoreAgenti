package com.example.GestoreAgenti.event.email; // Definisce il pacchetto com.example.GestoreAgenti.event.email che contiene questa classe.

import java.time.Instant; // Importa java.time.Instant per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEvent; // Importa com.example.GestoreAgenti.event.DomainEvent per abilitare le funzionalità utilizzate nel file.

public record EmailSentEvent(String from, // Definisce la record EmailSentEvent che incapsula la logica applicativa.
                             String to, // Esegue l'istruzione necessaria alla logica applicativa.
                             String subject, // Esegue l'istruzione necessaria alla logica applicativa.
                             Instant occurredOn) implements DomainEvent { // Apre il blocco di codice associato alla dichiarazione.

    public EmailSentEvent(String from, String to, String subject) { // Costruttore della classe EmailSentEvent che inizializza le dipendenze necessarie.
        this(from, to, subject, Instant.now()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
