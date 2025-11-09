package com.example.GestoreAgenti.event; // Definisce il pacchetto com.example.GestoreAgenti.event che contiene questa classe.

@FunctionalInterface // Applica l'annotazione @FunctionalInterface per configurare il componente.
public interface DomainEventListener<E extends DomainEvent> { // Definisce la interfaccia DomainEventListener che incapsula la logica applicativa.

    void onEvent(E event); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
