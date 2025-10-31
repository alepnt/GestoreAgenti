package com.example.GestoreAgenti.service.facade;

import java.util.List;
import java.util.Optional;

import com.example.GestoreAgenti.model.Dipendente;

/**
 * Contratto astratto che espone le operazioni di alto livello offerte dalla
 * facciata per il dominio agenti. Consente di sostituire l'implementazione
 * concreta con decoratori o proxy trasparenti per il controller REST.
 */
public interface AgentOperations {

    /**
     * Restituisce l'elenco completo dei dipendenti gestiti dal sistema.
     *
     * @return collezione non nulla di dipendenti
     */
    List<Dipendente> listAgents();

    /**
     * Recupera un dipendente in base all'identificativo fornito.
     *
     * @param id identificativo del dipendente
     * @return dipendente oppure {@code null} se inesistente
     */
    Dipendente getAgent(Long id);

    /**
     * Registra un nuovo dipendente nel dominio.
     *
     * @param dipendente entità da creare
     * @return entità persistita
     */
    Dipendente registerAgent(Dipendente dipendente);

    /**
     * Aggiorna lo stato del dipendente indicato.
     *
     * @param id identificativo del dipendente
     * @param dipendente dati aggiornati
     * @return entità salvata
     */
    Dipendente updateAgent(Long id, Dipendente dipendente);

    /**
     * Elimina il dipendente specificato.
     *
     * @param id identificativo del dipendente
     */
    void removeAgent(Long id);

    /**
     * Calcola una vista aggregata dell'attività del dipendente.
     *
     * @param id identificativo del dipendente
     * @return vista opzionale contenente i dati aggregati
     */
    Optional<AgentOverview> loadOverview(Long id);

    /**
     * Assegna il dipendente a un team esistente.
     *
     * @param agentId identificativo del dipendente
     * @param teamId identificativo del team
     * @return dipendente aggiornato
     */
    Dipendente assignAgentToTeam(Long agentId, Long teamId);

    /**
     * Costruisce la gerarchia dei team sfruttando il pattern Composite.
     *
     * @return radice della gerarchia rappresentata come nodo serializzabile
     */
    TeamHierarchyNode buildHierarchy();
}
