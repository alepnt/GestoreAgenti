package com.example.GestoreAgenti.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestoreAgenti.model.Team;
import com.example.GestoreAgenti.service.TeamService;

/**
 * Gestisce i team commerciali e le loro informazioni principali.
 */
@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService service;

    public TeamController(TeamService service) {
        this.service = service;
    }

    /** Restituisce tutti i team registrati. */
    @GetMapping
    public List<Team> getAllTeams() {
        return service.findAll();
    }

    /** Recupera un team in base all'identificativo. */
    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Crea un nuovo team commerciale. */
    @PostMapping
    public Team createTeam(@RequestBody Team team) {
        return service.save(team);
    }

    /** Aggiorna le informazioni del team indicato. */
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
        try {
            Team updated = service.update(id, team);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /** Elimina definitivamente il team indicato. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

