package com.example.GestoreAgenti.model.composite;

import java.util.Collections;
import java.util.List;

import com.example.GestoreAgenti.model.Dipendente;

/**
 * Foglia dell'albero che rappresenta un singolo agente.
 */
public class AgentLeaf implements TeamComponent {

    private final Dipendente agent;

    public AgentLeaf(Dipendente agent) {
        this.agent = agent;
    }

    public Dipendente getAgent() {
        return agent;
    }

    @Override
    public String getName() {
        String nome = agent.getNome() == null ? "" : agent.getNome();
        String cognome = agent.getCognome() == null ? "" : agent.getCognome();
        String fullName = (nome + " " + cognome).trim();
        if (!fullName.isEmpty()) {
            return fullName;
        }
        return agent.getUsername() != null ? agent.getUsername() : "Agente " + agent.getId();
    }

    @Override
    public int countMembers() {
        return 1;
    }

    @Override
    public List<Dipendente> getAgents() {
        return List.of(agent);
    }

    @Override
    public List<TeamComponent> getChildren() {
        return Collections.emptyList();
    }
}
