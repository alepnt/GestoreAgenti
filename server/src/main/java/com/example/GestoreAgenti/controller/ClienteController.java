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

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.service.ClienteService;

/**
 * Fornisce le operazioni CRUD sui clienti aziendali esposte via REST.
 */
@RestController
@RequestMapping("/api/clienti")
@CrossOrigin
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    /** Restituisce l'elenco completo dei clienti. */
    @GetMapping
    public List<Cliente> getAll() {
        return service.findAll();
    }

    /** Recupera un cliente tramite identificativo. */
    @GetMapping("/{id}")
    public Cliente getById(@PathVariable Long id) {
        return service.findById(id);
    }

    /** Registra un nuovo cliente. */
    @PostMapping
    public Cliente create(@RequestBody Cliente cliente) {
        return service.save(cliente);
    }

    /** Aggiorna i dati del cliente indicato. */
    @PutMapping("/{id}")
    public Cliente update(@PathVariable Long id, @RequestBody Cliente cliente) {
        return service.update(id, cliente);
    }

    /** Elimina definitivamente il cliente indicato. */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
