package com.example.GestoreAgenti.model.composite;

import java.util.List;

import com.example.GestoreAgenti.model.Dipendente;

/**
 * Componente dell'albero gerarchico dei team secondo il pattern Composite.
 */
public interface TeamComponent {

    /**
     * @return nome leggibile del nodo
     */
    String getName();

    /**
     * @return numero di agenti contenuti nel sottoalbero
     */
    int countMembers();

    /**
     * @return collezione piatta di agenti presenti nel sottoalbero
     */
    List<Dipendente> getAgents();

    /**
     * @return figli diretti del nodo (lista vuota per le foglie)
     */
    List<TeamComponent> getChildren();
}
