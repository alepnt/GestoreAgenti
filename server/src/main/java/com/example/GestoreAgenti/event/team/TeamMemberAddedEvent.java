package com.example.GestoreAgenti.event.team; // Definisce il pacchetto com.example.GestoreAgenti.event.team che contiene questa classe.

import java.time.Instant; // Importa java.time.Instant per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEvent; // Importa com.example.GestoreAgenti.event.DomainEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.

public record TeamMemberAddedEvent(Dipendente member, Instant occurredOn) implements DomainEvent { // Definisce la record TeamMemberAddedEvent che incapsula la logica applicativa.

    public TeamMemberAddedEvent(Dipendente member) { // Costruttore della classe TeamMemberAddedEvent che inizializza le dipendenze necessarie.
        this(member, Instant.now()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
