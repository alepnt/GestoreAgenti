package com.example.GestoreAgenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Servizio;
import com.example.GestoreAgenti.repository.ServizioRepository;

@Service
public class ServizioService {

    private final ServizioRepository repository;

    public ServizioService(ServizioRepository repository) {
        this.repository = repository;
    }

    public List<Servizio> getAllServizi() {
        return repository.findAll();
    }

    public Optional<Servizio> getServizioById(Long id) {
        return repository.findById(id);
    }

    public Servizio createServizio(Servizio servizio) {
        return repository.save(servizio);
    }

    public Servizio updateServizio(Long id, Servizio servizioDetails) {
        return repository.findById(id).map(servizio -> {
            servizio.setNome(servizioDetails.getNome());
            servizio.setDescrizione(servizioDetails.getDescrizione());
            servizio.setPrezzoBase(servizioDetails.getPrezzoBase());
            servizio.setCommissionePercentuale(servizioDetails.getCommissionePercentuale());
            return repository.save(servizio);
        }).orElseThrow(() -> new RuntimeException("Servizio non trovato con id " + id));
    }

    public void deleteServizio(Long id) {
        repository.deleteById(id);
    }
}

