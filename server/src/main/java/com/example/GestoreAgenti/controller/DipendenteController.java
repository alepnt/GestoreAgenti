package com.example.GestoreAgenti.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.service.facade.AgentOperations;
import com.example.GestoreAgenti.service.facade.AgentOverview;
import com.example.GestoreAgenti.service.facade.TeamHierarchyNode;

/**
 * Coordina le operazioni sui dipendenti, includendo gestione team e panoramiche
 * gerarchiche.
 */
@RestController
@RequestMapping("/api/dipendenti")
@CrossOrigin
public class DipendenteController {

    private final AgentOperations agentOperations;

    public DipendenteController(AgentOperations agentOperations) {
        this.agentOperations = agentOperations;
    }

    /** Restituisce la lista completa dei dipendenti. */
    @GetMapping
    public List<Dipendente> getAll() {
        return agentOperations.listAgents();
    }

    /** Recupera il dipendente richiesto, segnalando 404 se non presente. */
    @GetMapping("/{id}")
    public Dipendente getById(@PathVariable Long id) {
        Dipendente dipendente = agentOperations.getAgent(id);
        if (dipendente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dipendente non trovato");
        }
        return dipendente;
    }

    /** Registra un nuovo dipendente. */
    @PostMapping
    public Dipendente create(@RequestBody Dipendente dipendente) {
        return agentOperations.registerAgent(dipendente);
    }

    /** Aggiorna i dati di un dipendente esistente. */
    @PutMapping("/{id}")
    public Dipendente update(@PathVariable Long id, @RequestBody Dipendente dipendente) {
        return agentOperations.updateAgent(id, dipendente);
    }

    /** Rimuove definitivamente il dipendente indicato. */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        agentOperations.removeAgent(id);
    }

    /** Restituisce una vista aggregata sulle performance del dipendente. */
    @GetMapping("/{id}/overview")
    public AgentOverview getOverview(@PathVariable Long id) {
        return agentOperations.loadOverview(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Overview non disponibile"));
    }

    /** Assegna il dipendente a un team esistente gestendo errori di risorsa mancante. */
    @PostMapping("/{id}/teams/{teamId}")
    public Dipendente assignTeam(@PathVariable Long id, @PathVariable Long teamId) {
        try {
            return agentOperations.assignAgentToTeam(id, teamId);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    /** Restituisce la struttura gerarchica dei team e degli agenti. */
    @GetMapping("/gerarchia")
    public TeamHierarchyNode getHierarchy() {
        return agentOperations.buildHierarchy();
    }
}
