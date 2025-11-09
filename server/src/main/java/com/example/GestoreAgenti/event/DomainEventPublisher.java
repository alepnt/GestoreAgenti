package com.example.GestoreAgenti.event; // Definisce il pacchetto com.example.GestoreAgenti.event che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Map; // Importa java.util.Map per abilitare le funzionalità utilizzate nel file.
import java.util.concurrent.ConcurrentHashMap; // Importa java.util.concurrent.ConcurrentHashMap per abilitare le funzionalità utilizzate nel file.
import java.util.concurrent.CopyOnWriteArrayList; // Importa java.util.concurrent.CopyOnWriteArrayList per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Component; // Importa org.springframework.stereotype.Component per abilitare le funzionalità utilizzate nel file.

/**
 * Simple in-memory event bus used to decouple the publishing service from the
 * listeners interested in reacting to domain events.
 */
@Component // Applica l'annotazione @Component per configurare il componente.
public class DomainEventPublisher { // Definisce la classe DomainEventPublisher che incapsula la logica applicativa.

    private final Map<Class<?>, List<DomainEventListener<?>>> listeners = new ConcurrentHashMap<>(); // Definisce il metodo ConcurrentHashMap<> che supporta la logica di dominio.

    public <E extends DomainEvent> AutoCloseable subscribe(Class<E> eventType, DomainEventListener<E> listener) { // Definisce il metodo subscribe che supporta la logica di dominio.
        listeners.computeIfAbsent(eventType, key -> new CopyOnWriteArrayList<>()).add(listener); // Esegue l'istruzione terminata dal punto e virgola.
        return () -> listeners.getOrDefault(eventType, List.of()).remove(listener); // Restituisce il risultato dell'espressione () -> listeners.getOrDefault(eventType, List.of()).remove(listener).
    } // Chiude il blocco di codice precedente.

    public void publish(DomainEvent event) { // Definisce il metodo publish che supporta la logica di dominio.
        Class<?> eventClass = event.getClass(); // Assegna il valore calcolato alla variabile Class<?> eventClass.
        listeners.forEach((type, registeredListeners) -> { // Apre il blocco di codice associato alla dichiarazione.
            if (type.isAssignableFrom(eventClass)) { // Valuta la condizione per controllare il flusso applicativo.
                for (DomainEventListener<?> listener : registeredListeners) { // Itera sugli elementi richiesti dalla logica.
                    @SuppressWarnings("unchecked") // Applica l'annotazione @SuppressWarnings per configurare il componente.
                    DomainEventListener<DomainEvent> typed = (DomainEventListener<DomainEvent>) listener; // Assegna il valore calcolato alla variabile DomainEventListener<DomainEvent> typed.
                    typed.onEvent(event); // Esegue l'istruzione terminata dal punto e virgola.
                } // Chiude il blocco di codice precedente.
            } // Chiude il blocco di codice precedente.
        }); // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
