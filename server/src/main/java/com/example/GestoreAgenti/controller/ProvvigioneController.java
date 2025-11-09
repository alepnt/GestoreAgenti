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

import com.example.GestoreAgenti.model.Provvigione;
import com.example.GestoreAgenti.service.ProvvigioneService;

/**
 * Gestisce le provvigioni associate ai contratti degli agenti.
 */
@RestController
@RequestMapping("/api/provvigioni")
public class ProvvigioneController {

    private final ProvvigioneService service;

    public ProvvigioneController(ProvvigioneService service) {
        this.service = service;
    }

    /** Restituisce tutte le provvigioni. */
    @GetMapping
    public List<Provvigione> getAllProvvigioni() {
        return service.getAllProvvigioni();
    }

    /** Recupera una provvigione tramite identificativo. */
    @GetMapping("/{id}")
    public ResponseEntity<Provvigione> getProvvigioneById(@PathVariable Long id) {
        return service.getProvvigioneById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Registra una nuova provvigione. */
    @PostMapping
    public Provvigione createProvvigione(@RequestBody Provvigione provvigione) {
        return service.createProvvigione(provvigione);
    }

    /** Aggiorna la provvigione indicata. */
    @PutMapping("/{id}")
    public Provvigione updateProvvigione(@PathVariable Long id, @RequestBody Provvigione provvigione) {
        return service.updateProvvigione(id, provvigione);
    }

    /** Elimina definitivamente la provvigione indicata. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvvigione(@PathVariable Long id) {
        service.deleteProvvigione(id);
        return ResponseEntity.noContent().build();
    }
}
