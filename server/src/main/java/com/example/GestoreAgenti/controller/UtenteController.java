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

import com.example.GestoreAgenti.model.Utente; // Importa la classe Utente per utilizzare i dati anagrafici degli utenti.
import com.example.GestoreAgenti.service.UtenteService; // Importa UtenteService per utilizzare la logica applicativa sugli utenti.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/utenti") // Applica l'annotazione @RequestMapping per configurare il componente.
public class UtenteController { // Dichiara la classe UtenteController che incapsula la logica del dominio.

    private final UtenteService service; // Mantiene il riferimento al servizio applicativo UtenteService per delegare le operazioni di business.

    public UtenteController(UtenteService service) { // Costruttore della classe UtenteController che inizializza le dipendenze richieste.
        this.service = service; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Utente> getAllUtenti() { // Restituisce la lista di i utenti gestiti dal sistema.
        return service.getAllUtenti(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @GetMapping("/{id}") // Applica l'annotazione @GetMapping per configurare il componente.
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) { // Restituisce i dati di utente filtrati in base a ID.
        return service.getUtenteById(id) // Restituisce il risultato dell'elaborazione al chiamante.
                .map(ResponseEntity::ok) // Gestisce la risposta HTTP per l'endpoint REST.
                .orElse(ResponseEntity.notFound().build()); // Gestisce la risposta HTTP per l'endpoint REST.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Utente createUtente(@RequestBody Utente utente) { // Metodo create utente che gestisce la logica prevista.
        return service.createUtente(utente); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public Utente updateUtente(@PathVariable Long id, @RequestBody Utente utente) { // Aggiorna le utente applicando i dati forniti.
        return service.updateUtente(id, utente); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) { // Elimina le utente identificato dall'input.
        service.deleteUtente(id); // Esegue questa istruzione come parte della logica del metodo.
        return ResponseEntity.noContent().build(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

