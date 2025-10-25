package com.example.GestoreAgenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.repository.FatturaRepository;

@Service
public class FatturaService {

    private final FatturaRepository repository;

    public FatturaService(FatturaRepository repository) {
        this.repository = repository;
    }

    public List<Fattura> getAllFatture() {
        return repository.findAll();
    }

    public Optional<Fattura> getFatturaById(Long id) {
        return repository.findById(id);
    }

    public Fattura createFattura(Fattura fattura) {
        return repository.save(fattura);
    }

    public Fattura updateFattura(Long id, Fattura fatturaDetails) {
        return repository.findById(id).map(fattura -> {
            fattura.setNumeroFattura(fatturaDetails.getNumeroFattura());
            fattura.setDataEmissione(fatturaDetails.getDataEmissione());
            fattura.setCliente(fatturaDetails.getCliente());
            fattura.setContratto(fatturaDetails.getContratto());
            fattura.setImponibile(fatturaDetails.getImponibile());
            fattura.setIva(fatturaDetails.getIva());
            fattura.setTotale(fatturaDetails.getTotale());
            fattura.setStato(fatturaDetails.getStato());
            return repository.save(fattura);
        }).orElseThrow(() -> new RuntimeException("Fattura non trovata con id " + id));
    }

    public void deleteFattura(Long id) {
        repository.deleteById(id);
    }
}

