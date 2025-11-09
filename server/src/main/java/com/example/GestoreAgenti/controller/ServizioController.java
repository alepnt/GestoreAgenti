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

import com.example.GestoreAgenti.model.Servizio;
import com.example.GestoreAgenti.service.ServizioService;

/**
 * Gestisce il catalogo dei servizi offerti dall'azienda.
 */
@RestController
@RequestMapping("/api/servizi")
public class ServizioController {

    private final ServizioService service;

    public ServizioController(ServizioService service) {
        this.service = service;
    }

    /** Restituisce tutti i servizi disponibili. */
    @GetMapping
    public List<Servizio> getAllServizi() {
        return service.getAllServizi();
    }

    /** Recupera i dettagli di un servizio. */
    @GetMapping("/{id}")
    public ResponseEntity<Servizio> getServizioById(@PathVariable Long id) {
        return service.getServizioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Inserisce un nuovo servizio nel catalogo. */
    @PostMapping
    public Servizio createServizio(@RequestBody Servizio servizio) {
        return service.createServizio(servizio);
    }

    /** Aggiorna i dettagli di un servizio esistente. */
    @PutMapping("/{id}")
    public Servizio updateServizio(@PathVariable Long id, @RequestBody Servizio servizio) {
        return service.updateServizio(id, servizio);
    }

    /** Rimuove il servizio dal catalogo. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServizio(@PathVariable Long id) {
        service.deleteServizio(id);
        return ResponseEntity.noContent().build();
    }
}

