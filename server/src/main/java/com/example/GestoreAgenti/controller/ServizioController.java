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

import com.example.GestoreAgenti.model.Servizio; // Importa la classe Servizio per utilizzare i dettagli dei servizi commercializzati.
import com.example.GestoreAgenti.service.ServizioService; // Importa ServizioService per coinvolgere la logica sui servizi.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/servizi") // Applica l'annotazione @RequestMapping per configurare il componente.
public class ServizioController { // Dichiara la classe ServizioController che incapsula la logica del dominio.

    private final ServizioService service; // Mantiene il riferimento al servizio applicativo ServizioService per delegare le operazioni di business.

    public ServizioController(ServizioService service) { // Costruttore della classe ServizioController che inizializza le dipendenze richieste.
        this.service = service; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Servizio> getAllServizi() { // Restituisce la lista di i servizi gestiti dal sistema.
        return service.getAllServizi(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @GetMapping("/{id}") // Applica l'annotazione @GetMapping per configurare il componente.
    public ResponseEntity<Servizio> getServizioById(@PathVariable Long id) { // Restituisce i dati di servizio filtrati in base a ID.
        return service.getServizioById(id) // Restituisce il risultato dell'elaborazione al chiamante.
                .map(ResponseEntity::ok) // Gestisce la risposta HTTP per l'endpoint REST.
                .orElse(ResponseEntity.notFound().build()); // Gestisce la risposta HTTP per l'endpoint REST.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Servizio createServizio(@RequestBody Servizio servizio) { // Metodo create servizio che gestisce la logica prevista.
        return service.createServizio(servizio); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public Servizio updateServizio(@PathVariable Long id, @RequestBody Servizio servizio) { // Aggiorna il servizio applicando i dati forniti.
        return service.updateServizio(id, servizio); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public ResponseEntity<Void> deleteServizio(@PathVariable Long id) { // Elimina il servizio identificato dall'input.
        service.deleteServizio(id); // Esegue questa istruzione come parte della logica del metodo.
        return ResponseEntity.noContent().build(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

