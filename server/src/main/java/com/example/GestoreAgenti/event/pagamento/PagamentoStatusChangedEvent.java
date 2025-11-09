package com.example.GestoreAgenti.event.pagamento; // Definisce il pacchetto com.example.GestoreAgenti.event.pagamento che contiene questa classe.

import java.time.Instant; // Importa java.time.Instant per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEvent; // Importa com.example.GestoreAgenti.event.DomainEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.

public record PagamentoStatusChangedEvent(Pagamento pagamento, // Definisce la record PagamentoStatusChangedEvent che incapsula la logica applicativa.
                                          String previousStatus, // Esegue l'istruzione necessaria alla logica applicativa.
                                          String newStatus, // Esegue l'istruzione necessaria alla logica applicativa.
                                          Instant occurredOn) implements DomainEvent { // Apre il blocco di codice associato alla dichiarazione.

    public PagamentoStatusChangedEvent(Pagamento pagamento, String previousStatus, String newStatus) { // Costruttore della classe PagamentoStatusChangedEvent che inizializza le dipendenze necessarie.
        this(pagamento, previousStatus, newStatus, Instant.now()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
