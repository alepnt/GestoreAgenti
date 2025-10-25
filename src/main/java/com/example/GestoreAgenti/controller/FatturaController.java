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

import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.service.FatturaService;

@RestController
@RequestMapping("/api/fatture")
public class FatturaController {

    private final FatturaService service;

    public FatturaController(FatturaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Fattura> getAllFatture() {
        return service.getAllFatture();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fattura> getFatturaById(@PathVariable Long id) {
        return service.getFatturaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Fattura createFattura(@RequestBody Fattura fattura) {
        return service.createFattura(fattura);
    }

    @PutMapping("/{id}")
    public Fattura updateFattura(@PathVariable Long id, @RequestBody Fattura fattura) {
        return service.updateFattura(id, fattura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) {
        service.deleteFattura(id);
        return ResponseEntity.noContent().build();
    }
}

