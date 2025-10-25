package com.example.GestoreAgenti.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public List<Cliente> findAll() { return repository.findAll(); }

    public Cliente findById(Long id) { return repository.findById(id).orElse(null); }

    public Cliente save(Cliente cliente) { return repository.save(cliente); }

    public Cliente update(Long id, Cliente nuovo) {
        Cliente esistente = findById(id);
        if (esistente == null) return null;
        nuovo.setId(id);
        return repository.save(nuovo);
    }

    public void delete(Long id) { repository.deleteById(id); }
}
