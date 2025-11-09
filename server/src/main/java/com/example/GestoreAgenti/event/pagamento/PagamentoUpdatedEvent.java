package com.example.GestoreAgenti.event.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.event.pagamento che contiene questa classe.

import java.time.Instant; // Importa java.time.Instant per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEvent; // Importa com.example.GestoreAgenti.event.DomainEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.

public record PagamentoUpdatedEvent(Pagamento pagamento, Instant occurredOn) implements DomainEvent { // Definisce la record PagamentoUpdatedEvent che incapsula la logica applicativa.

    public PagamentoUpdatedEvent(Pagamento pagamento) { // Costruttore della classe PagamentoUpdatedEvent che inizializza le dipendenze necessarie.
        this(pagamento, Instant.now()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
