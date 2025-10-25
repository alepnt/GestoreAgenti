package com.example.GestoreAgenti.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.repository.DipendenteRepository;

@Service
public class DipendenteService {

    private final DipendenteRepository repository;

    public DipendenteService(DipendenteRepository repository) {
        this.repository = repository;
    }

    public List<Dipendente> findAll() { return repository.findAll(); }

    public Dipendente findById(Long id) { return repository.findById(id).orElse(null); }

    public Dipendente save(Dipendente dipendente) { return repository.save(dipendente); }

    public Dipendente update(Long id, Dipendente nuovo) {
        Dipendente esistente = findById(id);
        if (esistente == null) return null;
        nuovo.setId(id);
        return repository.save(nuovo);
    }

    public void delete(Long id) { repository.deleteById(id); }
}
