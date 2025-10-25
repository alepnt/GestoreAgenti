package com.example.GestoreAgenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Provvigione;
import com.example.GestoreAgenti.repository.ProvvigioneRepository;

@Service
public class ProvvigioneService {

    private final ProvvigioneRepository repository;

    public ProvvigioneService(ProvvigioneRepository repository) {
        this.repository = repository;
    }

    public List<Provvigione> getAllProvvigioni() {
        return repository.findAll();
    }

    public Optional<Provvigione> getProvvigioneById(Long id) {
        return repository.findById(id);
    }

    public Provvigione createProvvigione(Provvigione provvigione) {
        return repository.save(provvigione);
    }

    public Provvigione updateProvvigione(Long id, Provvigione provvigioneDetails) {
        return repository.findById(id).map(provvigione -> {
            provvigione.setDipendente(provvigioneDetails.getDipendente());
            provvigione.setContratto(provvigioneDetails.getContratto());
            provvigione.setPercentuale(provvigioneDetails.getPercentuale());
            provvigione.setImporto(provvigioneDetails.getImporto());
            provvigione.setStato(provvigioneDetails.getStato());
            provvigione.setDataCalcolo(provvigioneDetails.getDataCalcolo());
            return repository.save(provvigione);
        }).orElseThrow(() -> new RuntimeException("Provvigione non trovata con id " + id));
    }

    public void deleteProvvigione(Long id) {
        repository.deleteById(id);
    }
}
