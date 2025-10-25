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

import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.service.ContrattoService;

@RestController
@RequestMapping("/api/contratti")
public class ContrattoController {

    private final ContrattoService service;

    public ContrattoController(ContrattoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Contratto> getAllContratti() {
        return service.getAllContratti();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contratto> getContrattoById(@PathVariable Long id) {
        return service.getContrattoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Contratto createContratto(@RequestBody Contratto contratto) {
        return service.createContratto(contratto);
    }

    @PutMapping("/{id}")
    public Contratto updateContratto(@PathVariable Long id, @RequestBody Contratto contratto) {
        return service.updateContratto(id, contratto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContratto(@PathVariable Long id) {
        service.deleteContratto(id);
        return ResponseEntity.noContent().build();
    }
}

