package com.example.GestoreAgenti.model.composite; // Definisce il pacchetto com.example.GestoreAgenti.model.composite che contiene questa classe.

import java.util.Collections; // Importa java.util.Collections per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.

/**
 * Foglia dell'albero che rappresenta un singolo agente.
 */
public class AgentLeaf implements TeamComponent { // Definisce la classe AgentLeaf che incapsula la logica applicativa.

    private final Dipendente agent; // Dichiara il campo agent dell'oggetto.

    public AgentLeaf(Dipendente agent) { // Costruttore della classe AgentLeaf che inizializza le dipendenze necessarie.
        this.agent = agent; // Aggiorna il campo agent dell'istanza.
    } // Chiude il blocco di codice precedente.

    public Dipendente getAgent() { // Definisce il metodo getAgent che supporta la logica di dominio.
        return agent; // Restituisce il risultato dell'espressione agent.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getName() { // Definisce il metodo getName che supporta la logica di dominio.
        String nome = agent.getNome() == null ? "" : agent.getNome(); // Assegna il valore calcolato alla variabile String nome.
        String cognome = agent.getCognome() == null ? "" : agent.getCognome(); // Assegna il valore calcolato alla variabile String cognome.
        String fullName = (nome + " " + cognome).trim(); // Assegna il valore calcolato alla variabile String fullName.
        if (!fullName.isEmpty()) { // Valuta la condizione per controllare il flusso applicativo.
            return fullName; // Restituisce il risultato dell'espressione fullName.
        } // Chiude il blocco di codice precedente.
        return agent.getUsername() != null ? agent.getUsername() : "Agente " + agent.getId(); // Restituisce il risultato dell'espressione agent.getUsername() != null ? agent.getUsername() : "Agente " + agent.getId().
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public int countMembers() { // Definisce il metodo countMembers che supporta la logica di dominio.
        return 1; // Restituisce il risultato dell'espressione 1.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public List<Dipendente> getAgents() { // Definisce il metodo getAgents che supporta la logica di dominio.
        return List.of(agent); // Restituisce il risultato dell'espressione List.of(agent).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public List<TeamComponent> getChildren() { // Definisce il metodo getChildren che supporta la logica di dominio.
        return Collections.emptyList(); // Restituisce il risultato dell'espressione Collections.emptyList().
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
