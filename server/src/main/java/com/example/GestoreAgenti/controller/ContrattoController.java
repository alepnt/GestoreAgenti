package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.

import org.springframework.core.io.ByteArrayResource; // Importa ByteArrayResource per restituire file binari al client.
import org.springframework.core.io.Resource; // Importa Resource per modellare la risposta binaria.
import org.springframework.http.HttpHeaders; // Importa HttpHeaders per configurare gli header della risposta.
import org.springframework.http.MediaType; // Importa MediaType per descrivere il tipo di contenuto del file.
import org.springframework.http.ResponseEntity; // Importa ResponseEntity per restituire risposte HTTP ricche di metadati.
import org.springframework.web.bind.annotation.DeleteMapping; // Importa DeleteMapping per mappare le richieste HTTP DELETE.
import org.springframework.web.bind.annotation.GetMapping; // Importa GetMapping per mappare le richieste HTTP GET.
import org.springframework.web.bind.annotation.PathVariable; // Importa PathVariable per leggere gli identificativi dalla rotta.
import org.springframework.web.bind.annotation.PostMapping; // Importa PostMapping per mappare le richieste HTTP POST.
import org.springframework.web.bind.annotation.PutMapping; // Importa PutMapping per mappare le richieste HTTP PUT.
import org.springframework.web.bind.annotation.RequestBody; // Importa RequestBody per deserializzare il corpo della richiesta.
import org.springframework.web.bind.annotation.RequestMapping; // Importa RequestMapping per definire il prefisso delle rotte del controller.
import org.springframework.web.bind.annotation.RestController; // Importa RestController per esporre il controller come componente REST.

import com.example.GestoreAgenti.model.Contratto; // Importa la classe Contratto per manipolare i contratti esposti dall'API.
import com.example.GestoreAgenti.service.ContrattoService; // Importa ContrattoService per utilizzare la logica dei contratti.
import com.example.GestoreAgenti.service.report.ContrattoPdfReportService; // Importa il servizio che produce il PDF del contratto.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/contratti") // Applica l'annotazione @RequestMapping per configurare il componente.
public class ContrattoController { // Dichiara la classe ContrattoController che incapsula la logica del dominio.

    private final ContrattoService service; // Mantiene il riferimento al servizio applicativo ContrattoService per delegare le operazioni di business.
    private final ContrattoPdfReportService pdfReportService; // Servizio incaricato di produrre il template PDF condiviso.

    public ContrattoController(ContrattoService service, ContrattoPdfReportService pdfReportService) { // Costruttore della classe ContrattoController che inizializza le dipendenze richieste.
        this.service = service; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.pdfReportService = pdfReportService; // Aggiorna il campo dell'istanza con il servizio dedicato al PDF.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Contratto> getAllContratti() { // Restituisce la lista di i contratti gestiti dal sistema.
        return service.getAllContratti(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @GetMapping("/{id}") // Applica l'annotazione @GetMapping per configurare il componente.
    public ResponseEntity<Contratto> getContrattoById(@PathVariable Long id) { // Restituisce i dati di contratto filtrati in base a ID.
        return service.getContrattoById(id) // Restituisce il risultato dell'elaborazione al chiamante.
                .map(ResponseEntity::ok) // Gestisce la risposta HTTP per l'endpoint REST.
                .orElse(ResponseEntity.notFound().build()); // Gestisce la risposta HTTP per l'endpoint REST.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Contratto createContratto(@RequestBody Contratto contratto) { // Metodo create contratto che gestisce la logica prevista.
        return service.createContratto(contratto); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public Contratto updateContratto(@PathVariable Long id, @RequestBody Contratto contratto) { // Aggiorna il contratto applicando i dati forniti.
        return service.updateContratto(id, contratto); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public ResponseEntity<Void> deleteContratto(@PathVariable Long id) { // Elimina il contratto identificato dall'input.
        service.deleteContratto(id); // Esegue questa istruzione come parte della logica del metodo.
        return ResponseEntity.noContent().build(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE) // Applica l'annotazione @GetMapping per esporre il template PDF del contratto.
    public ResponseEntity<Resource> esportaContrattoPdf(@PathVariable Long id) { // Restituisce il file PDF generato.
        Contratto contratto = service.findRequiredById(id); // Recupera il contratto da rappresentare.
        byte[] pdf = pdfReportService.generaPdf(contratto); // Genera il PDF applicando il template condiviso.
        String filename = "contratto-" + id + ".pdf"; // Suggerisce un nome file consistente.
        return ResponseEntity.ok() // Configura la risposta HTTP di successo.
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename) // Imposta il nome del file.
                .contentType(MediaType.APPLICATION_PDF) // Specifica il tipo di contenuto PDF.
                .body(new ByteArrayResource(pdf)); // Restituisce il file al client.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

