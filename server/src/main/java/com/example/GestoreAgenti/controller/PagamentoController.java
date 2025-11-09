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

import com.example.GestoreAgenti.model.Pagamento;
import com.example.GestoreAgenti.service.PagamentoService;

/**
 * Espone le operazioni sui pagamenti e le transizioni di stato legate al loro
 * ciclo di vita.
 */
@RestController
@RequestMapping("/api/pagamenti")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    /** Restituisce tutti i pagamenti gestiti dal sistema. */
    @GetMapping
    public List<Pagamento> getAllPagamenti() {
        return service.getAllPagamenti();
    }

    /** Recupera un pagamento per identificativo. */
    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> getPagamentoById(@PathVariable Long id) {
        return service.getPagamentoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Registra un nuovo pagamento. */
    @PostMapping
    public Pagamento createPagamento(@RequestBody Pagamento pagamento) {
        return service.createPagamento(pagamento);
    }

    /** Aggiorna i dati di un pagamento esistente. */
    @PutMapping("/{id}")
    public Pagamento updatePagamento(@PathVariable Long id, @RequestBody Pagamento pagamento) {
        return service.updatePagamento(id, pagamento);
    }

    /** Elimina definitivamente il pagamento indicato. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePagamento(@PathVariable Long id) {
        service.deletePagamento(id);
        return ResponseEntity.noContent().build();
    }

    /** Avvia l'elaborazione del pagamento. */
    @PostMapping("/{id}/elabora")
    public Pagamento avviaElaborazione(@PathVariable Long id) {
        return service.avviaElaborazione(id);
    }

    /** Conclude con successo l'elaborazione del pagamento. */
    @PostMapping("/{id}/completa")
    public Pagamento completaPagamento(@PathVariable Long id) {
        return service.completaPagamento(id);
    }

    /** Contrassegna il pagamento come fallito. */
    @PostMapping("/{id}/fallisci")
    public Pagamento fallisciPagamento(@PathVariable Long id) {
        return service.fallisciPagamento(id);
    }

    /** Riporta il pagamento allo stato iniziale per ripetere l'elaborazione. */
    @PostMapping("/{id}/ripeti")
    public Pagamento ripetiElaborazione(@PathVariable Long id) {
        return service.ripetiElaborazione(id);
    }
}
