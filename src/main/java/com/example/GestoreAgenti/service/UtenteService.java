package com.example.GestoreAgenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Utente;
import com.example.GestoreAgenti.repository.UtenteRepository;

@Service
public class UtenteService {

    private final UtenteRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UtenteService(UtenteRepository repository) {
        this.repository = repository;
    }

    public List<Utente> getAllUtenti() {
        return repository.findAll();
    }

    public Optional<Utente> getUtenteById(Long id) {
        return repository.findById(id);
    }

    public Utente createUtente(Utente utente) {
        utente.setPasswordHash(passwordEncoder.encode(utente.getPasswordHash()));
        return repository.save(utente);
    }

    public Utente updateUtente(Long id, Utente utenteDetails) {
        return repository.findById(id).map(utente -> {
            utente.setUsername(utenteDetails.getUsername());
            if (utenteDetails.getPasswordHash() != null && !utenteDetails.getPasswordHash().isEmpty()) {
                utente.setPasswordHash(passwordEncoder.encode(utenteDetails.getPasswordHash()));
            }
            utente.setRuolo(utenteDetails.getRuolo());
            utente.setDipendente(utenteDetails.getDipendente());
            return repository.save(utente);
        }).orElseThrow(() -> new RuntimeException("Utente non trovato con id " + id));
    }

    public void deleteUtente(Long id) {
        repository.deleteById(id);
    }
}

