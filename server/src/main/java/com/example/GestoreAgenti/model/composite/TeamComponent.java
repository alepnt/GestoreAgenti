package com.example.GestoreAgenti.model.composite; // Definisce il pacchetto com.example.GestoreAgenti.model.composite che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.

/**
 * Componente dell'albero gerarchico dei team secondo il pattern Composite.
 */
public interface TeamComponent { // Definisce la interfaccia TeamComponent che incapsula la logica applicativa.

    /**
     * @return nome leggibile del nodo
     */
    String getName(); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * @return numero di agenti contenuti nel sottoalbero
     */
    int countMembers(); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * @return collezione piatta di agenti presenti nel sottoalbero
     */
    List<Dipendente> getAgents(); // Esegue l'istruzione terminata dal punto e virgola.

    /**
     * @return figli diretti del nodo (lista vuota per le foglie)
     */
    List<TeamComponent> getChildren(); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
