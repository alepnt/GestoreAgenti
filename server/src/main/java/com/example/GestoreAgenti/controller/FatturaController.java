package com.example.GestoreAgenti.controller;

import java.time.LocalDate;
import java.util.List;

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

import com.example.GestoreAgenti.controller.dto.MonthlyRevenueDto;
import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.service.FatturaService;
import com.example.GestoreAgenti.service.report.FatturaExcelReportService;
import com.example.GestoreAgenti.service.report.FatturaPdfReportService;

/**
 * Gestisce le fatture offrendo CRUD, reportistica e transizioni di stato.
 */
@RestController
@RequestMapping("/api/fatture")
public class FatturaController {

    private static final MediaType EXCEL_MEDIA_TYPE = MediaType
            .parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final FatturaService service;
    private final FatturaExcelReportService reportService;
    private final FatturaPdfReportService pdfReportService;

    public FatturaController(FatturaService service, FatturaExcelReportService reportService,
            FatturaPdfReportService pdfReportService) {
        this.service = service;
        this.reportService = reportService;
        this.pdfReportService = pdfReportService;
    }

    /** Restituisce tutte le fatture presenti a sistema. */
    @GetMapping
    public List<Fattura> getAllFatture() {
        return service.getAllFatture();
    }

    @GetMapping("/vendita")
    public List<Fattura> getFattureVendita() {
        return service.getFattureVendita();
    }

    @GetMapping("/registrate")
    public List<Fattura> getFattureRegistrate() {
        return service.getFattureRegistrate();
    }

    @PostMapping("/{id}/registrazione")
    public Fattura registraFattura(@PathVariable Long id) {
        return service.registraFattura(id);
    }

    @DeleteMapping("/{id}/registrazione")
    public Fattura annullaRegistrazione(@PathVariable Long id) {
        return service.annullaRegistrazione(id);
    }

    @GetMapping("/andamento")
    public List<MonthlyRevenueDto> andamentoFatturato(
            @RequestParam(name = "dal", required = false) LocalDate from,
            @RequestParam(name = "al", required = false) LocalDate to) {
        return service.getAndamentoFatturato(from, to);
    }

    /** Esporta un report Excel contenente tutte le fatture. */
    @GetMapping(value = "/report", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<Resource> esportaReportFatture() {
        byte[] workbook = reportService.generaReport(service.getAllFatture());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fatture-report.xlsx")
                .contentType(EXCEL_MEDIA_TYPE)
                .body(new ByteArrayResource(workbook));
    }

    /** Produce il PDF della fattura indicata. */
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> esportaFatturaPdf(@PathVariable Long id) {
        Fattura fattura = service.findRequiredById(id);
        byte[] pdf = pdfReportService.generaPdf(fattura);
        String filename = "fattura-" + (fattura.getNumeroFattura() != null ? fattura.getNumeroFattura() : id) + ".pdf";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdf));
    }

    /** Restituisce la fattura individuata dall'ID specificato. */
    @GetMapping("/{id}")
    public ResponseEntity<Fattura> getFatturaById(@PathVariable Long id) {
        return service.getFatturaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Registra una nuova fattura. */
    @PostMapping
    public Fattura createFattura(@RequestBody Fattura fattura) {
        return service.createFattura(fattura);
    }

    /** Aggiorna le informazioni della fattura specificata. */
    @PutMapping("/{id}")
    public Fattura updateFattura(@PathVariable Long id, @RequestBody Fattura fattura) {
        return service.updateFattura(id, fattura);
    }

    /** Elimina la fattura indicata dal chiamante. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) {
        service.deleteFattura(id);
        return ResponseEntity.noContent().build();
    }

    /** Passa la fattura in stato "emessa" se consentito. */
    @PostMapping("/{id}/emetti")
    public Fattura emettiFattura(@PathVariable Long id) {
        return service.emettiFattura(id);
    }

    /** Segna la fattura come pagata applicando la logica di dominio. */
    @PostMapping("/{id}/paga")
    public Fattura pagaFattura(@PathVariable Long id) {
        return service.pagaFattura(id);
    }

    /** Annulla la fattura portandola nello stato dedicato. */
    @PostMapping("/{id}/annulla")
    public Fattura annullaFattura(@PathVariable Long id) {
        return service.annullaFattura(id);
    }
}

