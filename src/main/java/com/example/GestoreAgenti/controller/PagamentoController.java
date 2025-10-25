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

@RestController
@RequestMapping("/api/pagamenti")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pagamento> getAllPagamenti() {
        return service.getAllPagamenti();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> getPagamentoById(@PathVariable Long id) {
        return service.getPagamentoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Pagamento createPagamento(@RequestBody Pagamento pagamento) {
        return service.createPagamento(pagamento);
    }

    @PutMapping("/{id}")
    public Pagamento updatePagamento(@PathVariable Long id, @RequestBody Pagamento pagamento) {
        return service.updatePagamento(id, pagamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePagamento(@PathVariable Long id) {
        service.deletePagamento(id);
        return ResponseEntity.noContent().build();
    }
}
