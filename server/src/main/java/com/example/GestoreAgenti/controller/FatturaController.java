package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.

import org.springframework.http.ResponseEntity; // Importa ResponseEntity per restituire risposte HTTP ricche di metadati.
import org.springframework.web.bind.annotation.DeleteMapping; // Importa DeleteMapping per mappare le richieste HTTP DELETE.
import org.springframework.web.bind.annotation.GetMapping; // Importa GetMapping per mappare le richieste HTTP GET.
import org.springframework.web.bind.annotation.PathVariable; // Importa PathVariable per leggere gli identificativi dalla rotta.
import org.springframework.web.bind.annotation.PostMapping; // Importa PostMapping per mappare le richieste HTTP POST.
import org.springframework.web.bind.annotation.PutMapping; // Importa PutMapping per mappare le richieste HTTP PUT.
import org.springframework.web.bind.annotation.RequestBody; // Importa RequestBody per deserializzare il corpo della richiesta.
import org.springframework.web.bind.annotation.RequestMapping; // Importa RequestMapping per definire il prefisso delle rotte del controller.
import org.springframework.web.bind.annotation.RestController; // Importa RestController per esporre il controller come componente REST.

import com.example.GestoreAgenti.model.Fattura; // Importa la classe Fattura per accedere alle informazioni di fatturazione.
import com.example.GestoreAgenti.service.FatturaService; // Importa FatturaService per coordinare la gestione delle fatture.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/fatture") // Applica l'annotazione @RequestMapping per configurare il componente.
public class FatturaController { // Dichiara la classe FatturaController che incapsula la logica del dominio.

    private final FatturaService service; // Mantiene il riferimento al servizio applicativo FatturaService per delegare le operazioni di business.

    public FatturaController(FatturaService service) { // Costruttore della classe FatturaController che inizializza le dipendenze richieste.
        this.service = service; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Fattura> getAllFatture() { // Restituisce la lista di le fatture gestiti dal sistema.
        return service.getAllFatture(); // Restituisce il risultato dell'elaborazione al chiamante.
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

