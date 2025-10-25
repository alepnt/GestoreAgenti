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

import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.service.DipendenteService;

@RestController
@RequestMapping("/api/dipendenti")
@CrossOrigin
public class DipendenteController {

    private final DipendenteService service;

    public DipendenteController(DipendenteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Dipendente> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Dipendente getById(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public Dipendente create(@RequestBody Dipendente dipendente) { return service.save(dipendente); }

    @PutMapping("/{id}")
    public Dipendente update(@PathVariable Long id, @RequestBody Dipendente dipendente) {
        return service.update(id, dipendente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}
