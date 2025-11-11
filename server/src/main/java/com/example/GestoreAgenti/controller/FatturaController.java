package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller a cui appartiene questa classe.

import java.time.LocalDate;
import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Objects; // Importa Objects per gestire i controlli di nullità richiesti dalle API annotate @NonNull.

import org.springframework.core.io.ByteArrayResource; // Importa ByteArrayResource per fornire file binari come risposta.
import org.springframework.core.io.Resource; // Importa Resource per modellare la risposta del file.
import org.springframework.http.HttpHeaders; // Importa HttpHeaders per configurare gli header della risposta.
import org.springframework.http.MediaType; // Importa MediaType per descrivere il tipo di contenuto.
import org.springframework.http.ResponseEntity; // Importa ResponseEntity per restituire risposte HTTP ricche di metadati.
import org.springframework.web.bind.annotation.DeleteMapping; // Importa DeleteMapping per mappare le richieste HTTP DELETE.
import org.springframework.web.bind.annotation.GetMapping; // Importa GetMapping per mappare le richieste HTTP GET.
import org.springframework.web.bind.annotation.PathVariable; // Importa PathVariable per leggere gli identificativi dalla rotta.
import org.springframework.web.bind.annotation.PostMapping; // Importa PostMapping per mappare le richieste HTTP POST.
import org.springframework.web.bind.annotation.PutMapping; // Importa PutMapping per mappare le richieste HTTP PUT.
import org.springframework.web.bind.annotation.RequestBody; // Importa RequestBody per deserializzare il corpo della richiesta.
import org.springframework.web.bind.annotation.RequestMapping; // Importa RequestMapping per definire il prefisso delle rotte del controller.
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController; // Importa RestController per esporre il controller come componente REST.

import com.example.GestoreAgenti.controller.dto.MonthlyRevenueDto;
import com.example.GestoreAgenti.model.Fattura; // Importa la classe Fattura per accedere alle informazioni di fatturazione.
import com.example.GestoreAgenti.service.FatturaService; // Importa FatturaService per coordinare la gestione delle fatture.
import com.example.GestoreAgenti.service.report.FatturaExcelReportService; // Importa il servizio per generare il report Excel delle fatture.
import com.example.GestoreAgenti.service.report.FatturaPdfReportService; // Importa il servizio per generare il PDF delle fatture.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/fatture") // Applica l'annotazione @RequestMapping per configurare il componente.
public class FatturaController { // Dichiara la classe FatturaController che incapsula la logica del dominio.

    private static final MediaType EXCEL_MEDIA_TYPE = MediaType.parseMediaType( // Definisce il metodo MediaType.parseMediaType che supporta la logica di dominio.
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // Media type dell'Excel generato.

    private final FatturaService service; // Mantiene il riferimento al servizio applicativo FatturaService per delegare le operazioni di business.
    private final FatturaExcelReportService reportService; // Servizio per generare il layout Excel differenziato per tipo di fattura.
    private final FatturaPdfReportService pdfReportService; // Servizio per generare il template PDF condiviso delle fatture.

    public FatturaController(FatturaService service, FatturaExcelReportService reportService, // Definisce il metodo FatturaController che supporta la logica di dominio.
            FatturaPdfReportService pdfReportService) { // Costruttore della classe FatturaController che inizializza le dipendenze richieste.
        this.service = service; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.reportService = reportService; // Aggiorna il campo dell'istanza con il servizio dedicato ai report Excel.
        this.pdfReportService = pdfReportService; // Aggiorna il campo dell'istanza con il servizio dedicato al PDF.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Fattura> getAllFatture() { // Restituisce la lista di le fatture gestiti dal sistema.
        return service.getAllFatture(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

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

    @GetMapping(value = "/report", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") // Applica l'annotazione @GetMapping per esporre il report Excel.
    public ResponseEntity<Resource> esportaReportFatture() { // Restituisce il file Excel generato.
        byte[] workbook = Objects.requireNonNull(reportService.generaReport(service.getAllFatture()), "Il report generato non può essere nullo"); // Genera il report aggregando i layout differenti.
        return ResponseEntity.ok() // Configura la risposta HTTP di successo.
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fatture-report.xlsx") // Suggerisce il nome del file scaricato.
                .contentType(Objects.requireNonNull(EXCEL_MEDIA_TYPE)) // Imposta il tipo di contenuto dell'Excel.
                .body(new ByteArrayResource(workbook)); // Restituisce il file al client.
    } // Chiude il blocco di codice precedente.

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE) // Applica l'annotazione @GetMapping per esporre il report PDF della fattura.
    public ResponseEntity<Resource> esportaFatturaPdf(@PathVariable Long id) { // Restituisce il template PDF generato.
        Fattura fattura = service.findRequiredById(id); // Recupera la fattura da rappresentare.
        byte[] pdf = Objects.requireNonNull(pdfReportService.generaPdf(fattura), "Il PDF generato non può essere nullo"); // Genera il PDF utilizzando il template condiviso.
        String filename = "fattura-" + (fattura.getNumeroFattura() != null ? fattura.getNumeroFattura() : id) + ".pdf"; // Determina il nome del file.
        return ResponseEntity.ok() // Configura la risposta HTTP di successo.
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename) // Suggerisce il nome del file scaricato.
                .contentType(Objects.requireNonNull(MediaType.APPLICATION_PDF)) // Imposta il tipo di contenuto del PDF.
                .body(new ByteArrayResource(pdf)); // Restituisce il file al client.
    } // Chiude il blocco di codice precedente.

    @GetMapping("/{id}") // Applica l'annotazione @GetMapping per configurare il componente.
    public ResponseEntity<Fattura> getFatturaById(@PathVariable Long id) { // Restituisce i dati di fattura filtrati in base a ID.
        return service.getFatturaById(id) // Restituisce il risultato dell'elaborazione al chiamante.
                .map(ResponseEntity::ok) // Gestisce la risposta HTTP per l'endpoint REST.
                .orElse(ResponseEntity.notFound().build()); // Gestisce la risposta HTTP per l'endpoint REST.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Fattura createFattura(@RequestBody Fattura fattura) { // Metodo create fattura che gestisce la logica prevista.
        return service.createFattura(fattura); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public Fattura updateFattura(@PathVariable Long id, @RequestBody Fattura fattura) { // Aggiorna la fattura applicando i dati forniti.
        return service.updateFattura(id, fattura); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public ResponseEntity<Void> deleteFattura(@PathVariable Long id) { // Elimina la fattura identificato dall'input.
        service.deleteFattura(id); // Esegue questa istruzione come parte della logica del metodo.
        return ResponseEntity.noContent().build(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @PostMapping("/{id}/emetti") // Applica l'annotazione @PostMapping per configurare il componente.
    public Fattura emettiFattura(@PathVariable Long id) { // Richiede l'emissione della fattura rispettando le regole di stato.
        return service.emettiFattura(id); // Delega la logica di transizione al servizio applicativo.
    } // Chiude il blocco di codice precedente.

    @PostMapping("/{id}/paga") // Applica l'annotazione @PostMapping per configurare il componente.
    public Fattura pagaFattura(@PathVariable Long id) { // Richiede il pagamento della fattura rispettando le regole di stato.
        return service.pagaFattura(id); // Delega la logica di transizione al servizio applicativo.
    } // Chiude il blocco di codice precedente.

    @PostMapping("/{id}/annulla") // Applica l'annotazione @PostMapping per configurare il componente.
    public Fattura annullaFattura(@PathVariable Long id) { // Richiede l'annullamento della fattura rispettando le regole di stato.
        return service.annullaFattura(id); // Delega la logica di transizione al servizio applicativo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

