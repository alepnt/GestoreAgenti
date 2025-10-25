package com.example.GestoreAgenti.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Pagamento;
import com.example.GestoreAgenti.repository.PagamentoRepository;

@Service
public class PagamentoService {

    private final PagamentoRepository repository;

    public PagamentoService(PagamentoRepository repository) {
        this.repository = repository;
    }

    public List<Pagamento> getAllPagamenti() {
        return repository.findAll();
    }

    public Optional<Pagamento> getPagamentoById(Long id) {
        return repository.findById(id);
    }

    public Pagamento createPagamento(Pagamento pagamento) {
        return repository.save(pagamento);
    }

    public Pagamento updatePagamento(Long id, Pagamento pagamentoDetails) {
        return repository.findById(id).map(pagamento -> {
            pagamento.setFattura(pagamentoDetails.getFattura());
            pagamento.setDataPagamento(pagamentoDetails.getDataPagamento());
            pagamento.setImporto(pagamentoDetails.getImporto());
            pagamento.setMetodo(pagamentoDetails.getMetodo());
            return repository.save(pagamento);
        }).orElseThrow(() -> new RuntimeException("Pagamento non trovato con id " + id));
    }

    public void deletePagamento(Long id) {
        repository.deleteById(id);
    }
}

