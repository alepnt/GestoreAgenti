package com.example.GestoreAgenti.service.facade; // Definisce il pacchetto com.example.GestoreAgenti.service.facade che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.

/**
 * Contratto astratto che espone le operazioni di alto livello offerte dalla
 * facciata per il dominio agenti. Consente di sostituire l'implementazione
 * concreta con decoratori o proxy trasparenti per il controller REST.
 */
public interface AgentOperations { // Definisce la interfaccia AgentOperations che incapsula la logica applicativa.

    /**
     * Restituisce l'elenco completo dei dipendenti gestiti dal sistema.
     *
     * @return collezione non nulla di dipendenti
     */
    List<Dipendente> listAgents(); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Recupera un dipendente in base all'identificativo fornito.
     *
     * @param id identificativo del dipendente
     * @return dipendente oppure {@code null} se inesistente
     */
    Dipendente getAgent(Long id); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Registra un nuovo dipendente nel dominio.
     *
     * @param dipendente entità da creare
     * @return entità persistita
     */
    Dipendente registerAgent(Dipendente dipendente); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Aggiorna lo stato del dipendente indicato.
     *
     * @param id identificativo del dipendente
     * @param dipendente dati aggiornati
     * @return entità salvata
     */
    Dipendente updateAgent(Long id, Dipendente dipendente); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Elimina il dipendente specificato.
     *
     * @param id identificativo del dipendente
     */
    void removeAgent(Long id); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Calcola una vista aggregata dell'attività del dipendente.
     *
     * @param id identificativo del dipendente
     * @return vista opzionale contenente i dati aggregati
     */
    Optional<AgentOverview> loadOverview(Long id); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Assegna il dipendente a un team esistente.
     *
     * @param agentId identificativo del dipendente
     * @param teamId identificativo del team
     * @return dipendente aggiornato
     */
    Dipendente assignAgentToTeam(Long agentId, Long teamId); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * Costruisce la gerarchia dei team sfruttando il pattern Composite.
     *
     * @return radice della gerarchia rappresentata come nodo serializzabile
     */
    TeamHierarchyNode buildHierarchy(); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
