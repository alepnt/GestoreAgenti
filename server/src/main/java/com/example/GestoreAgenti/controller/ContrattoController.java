package com.example.GestoreAgenti.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.service.ContrattoService;
import com.example.GestoreAgenti.service.report.ContrattoPdfReportService;

/**
 * Espone le operazioni REST dedicate ai contratti, inclusa l'esportazione dello
 * storico e del PDF.
 */
@RestController
@RequestMapping("/api/contratti")
public class ContrattoController {

    private final ContrattoService service;
    private final ContrattoPdfReportService pdfReportService;

    public ContrattoController(ContrattoService service, ContrattoPdfReportService pdfReportService) {
        this.service = service;
        this.pdfReportService = pdfReportService;
    }

    /** Restituisce tutti i contratti gestiti dal sistema. */
    @GetMapping
    public List<Contratto> getAllContratti() {
        return service.getAllContratti();
    }

    @GetMapping("/storico/dipendenti/{dipendenteId}")
    public List<ContractHistoryResponse> getStoricoPerDipendente(@PathVariable Long dipendenteId) {
        return service.getContrattiByDipendente(dipendenteId).stream()
                .map(ContrattoController::toHistoryResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/storico/clienti/{clienteId}")
    public List<ContractHistoryResponse> getStoricoPerCliente(@PathVariable Long clienteId) {
        return service.getContrattiByCliente(clienteId).stream()
                .map(ContrattoController::toHistoryResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/storico/team")
    public List<ContractHistoryResponse> getStoricoPerTeam(@RequestParam("nome") String team) {
        return service.getContrattiByTeam(team).stream()
                .map(ContrattoController::toHistoryResponse)
                .collect(Collectors.toList());
    }

    /** Restituisce il contratto identificato dall'ID fornito. */
    @GetMapping("/{id}")
    public ResponseEntity<Contratto> getContrattoById(@PathVariable Long id) {
        return service.getContrattoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Crea un nuovo contratto con i dati ricevuti dal client. */
    @PostMapping
    public Contratto createContratto(@RequestBody Contratto contratto) {
        return service.createContratto(contratto);
    }

    /** Aggiorna un contratto esistente sovrascrivendo i dati con quelli ricevuti. */
    @PutMapping("/{id}")
    public Contratto updateContratto(@PathVariable Long id, @RequestBody Contratto contratto) {
        return service.updateContratto(id, contratto);
    }

    /** Rimuove il contratto indicato e restituisce una risposta senza contenuto. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContratto(@PathVariable Long id) {
        service.deleteContratto(id);
        return ResponseEntity.noContent().build();
    }

    /** Esporta il contratto richiesto in formato PDF. */
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> esportaContrattoPdf(@PathVariable Long id) {
        Contratto contratto = service.findRequiredById(id);
        byte[] pdf = pdfReportService.generaPdf(contratto);
        String filename = "contratto-" + id + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdf));
    }

    private static ContractHistoryResponse toHistoryResponse(Contratto contratto) {
        String cliente = contratto.getCliente() != null
                ? String.join(" ",
                        nonNullOrEmpty(contratto.getCliente().getNome()),
                        nonNullOrEmpty(contratto.getCliente().getCognome()),
                        nonNullOrEmpty(contratto.getCliente().getRagioneSociale()))
                : null;
        String dipendente = contratto.getDipendente() != null
                ? String.join(" ",
                        nonNullOrEmpty(contratto.getDipendente().getNome()),
                        nonNullOrEmpty(contratto.getDipendente().getCognome()))
                : null;
        String team = contratto.getDipendente() != null ? contratto.getDipendente().getTeam() : null;
        return new ContractHistoryResponse(contratto.getIdContratto(),
                contratto.getCliente() != null ? contratto.getCliente().getId() : null,
                cliente != null ? cliente.trim() : null,
                contratto.getDipendente() != null ? contratto.getDipendente().getId() : null,
                dipendente != null ? dipendente.trim() : null,
                team, contratto.getServizio() != null ? contratto.getServizio().getNome() : null,
                contratto.getDataInizio(), contratto.getDataFine(), contratto.getImporto(), contratto.getStato());
    }

    private static String nonNullOrEmpty(String value) {
        return value == null ? "" : value;
    }

    public record ContractHistoryResponse(Long id, Long clienteId, String cliente,
            Long dipendenteId, String dipendente, String team,
            String servizio, java.time.LocalDate dataInizio, java.time.LocalDate dataFine,
            java.math.BigDecimal importo, String stato) {
    }
}

