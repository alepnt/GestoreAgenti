package com.example.GestoreAgenti.event.chat; // Definisce il pacchetto com.example.GestoreAgenti.event.chat che contiene questa classe.

import java.time.Instant; // Importa java.time.Instant per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEvent; // Importa com.example.GestoreAgenti.event.DomainEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.ChatMessage; // Importa com.example.GestoreAgenti.model.ChatMessage per abilitare le funzionalità utilizzate nel file.

public record ChatMessageCreatedEvent(ChatMessage message, Instant occurredOn) implements DomainEvent { // Definisce la record ChatMessageCreatedEvent che incapsula la logica applicativa.

    public ChatMessageCreatedEvent(ChatMessage message) { // Costruttore della classe ChatMessageCreatedEvent che inizializza le dipendenze necessarie.
        this(message, Instant.now()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
