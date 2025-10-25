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

import com.example.GestoreAgenti.model.Utente;
import com.example.GestoreAgenti.service.UtenteService;

@RestController
@RequestMapping("/api/utenti")
public class UtenteController {

    private final UtenteService service;

    public UtenteController(UtenteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Utente> getAllUtenti() {
        return service.getAllUtenti();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) {
        return service.getUtenteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Utente createUtente(@RequestBody Utente utente) {
        return service.createUtente(utente);
    }

    @PutMapping("/{id}")
    public Utente updateUtente(@PathVariable Long id, @RequestBody Utente utente) {
        return service.updateUtente(id, utente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) {
        service.deleteUtente(id);
        return ResponseEntity.noContent().build();
    }
}

