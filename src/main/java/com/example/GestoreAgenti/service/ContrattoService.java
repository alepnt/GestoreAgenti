package com.example.GestoreAgenti.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.repository.ContrattoRepository;

@Service
public class ContrattoService {

    private final ContrattoRepository repository;

    public ContrattoService(ContrattoRepository repository) {
        this.repository = repository;
    }

    public List<Contratto> getAllContratti() {
        return repository.findAll();
    }

    public Optional<Contratto> getContrattoById(Long id) {
        return repository.findById(id);
    }

    public Contratto createContratto(Contratto contratto) {
        return repository.save(contratto);
    }

    public Contratto updateContratto(Long id, Contratto contrattoDetails) {
        return repository.findById(id).map(contratto -> {
            contratto.setCliente(contrattoDetails.getCliente());
            contratto.setDipendente(contrattoDetails.getDipendente());
            contratto.setServizio(contrattoDetails.getServizio());
            contratto.setDataInizio(contrattoDetails.getDataInizio());
            contratto.setDataFine(contrattoDetails.getDataFine());
            contratto.setImporto(contrattoDetails.getImporto());
            contratto.setStato(contrattoDetails.getStato());
            contratto.setNote(contrattoDetails.getNote());
            return repository.save(contratto);
        }).orElseThrow(() -> new RuntimeException("Contratto non trovato con id " + id));
    }

    public void deleteContratto(Long id) {
        repository.deleteById(id);
    }
}
