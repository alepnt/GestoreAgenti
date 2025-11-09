package com.example.GestoreAgenti.event; // Definisce il pacchetto com.example.GestoreAgenti.event che contiene questa classe.

import java.time.Instant; // Importa java.time.Instant per abilitare le funzionalità utilizzate nel file.

/**
 * Marker interface for domain events published by the backend.
 */
public interface DomainEvent { // Definisce la interfaccia DomainEvent che incapsula la logica applicativa.

    Instant occurredOn(); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
