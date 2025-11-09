package com.example.GestoreAgenti.event.fattura; // Definisce il pacchetto com.example.GestoreAgenti.event.fattura che contiene questa classe.

import java.time.Instant; // Importa java.time.Instant per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEvent; // Importa com.example.GestoreAgenti.event.DomainEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.

public record FatturaCreatedEvent(Fattura fattura, Instant occurredOn) implements DomainEvent { // Definisce la record FatturaCreatedEvent che incapsula la logica applicativa.

    public FatturaCreatedEvent(Fattura fattura) { // Costruttore della classe FatturaCreatedEvent che inizializza le dipendenze necessarie.
        this(fattura, Instant.now()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
