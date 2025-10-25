package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.

import org.springframework.web.bind.annotation.CrossOrigin; // Importa CrossOrigin per abilitare le richieste CORS verso il controller.
import org.springframework.web.bind.annotation.DeleteMapping; // Importa DeleteMapping per mappare le richieste HTTP DELETE.
import org.springframework.web.bind.annotation.GetMapping; // Importa GetMapping per mappare le richieste HTTP GET.
import org.springframework.web.bind.annotation.PathVariable; // Importa PathVariable per leggere gli identificativi dalla rotta.
import org.springframework.web.bind.annotation.PostMapping; // Importa PostMapping per mappare le richieste HTTP POST.
import org.springframework.web.bind.annotation.PutMapping; // Importa PutMapping per mappare le richieste HTTP PUT.
import org.springframework.web.bind.annotation.RequestBody; // Importa RequestBody per deserializzare il corpo della richiesta.
import org.springframework.web.bind.annotation.RequestMapping; // Importa RequestMapping per definire il prefisso delle rotte del controller.
import org.springframework.web.bind.annotation.RestController; // Importa RestController per esporre il controller come componente REST.

import com.example.GestoreAgenti.model.Dipendente; // Importa la classe Dipendente per operare sui dati dei dipendenti.
import com.example.GestoreAgenti.service.DipendenteService; // Importa DipendenteService per orchestrare le operazioni sui dipendenti.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/dipendenti") // Applica l'annotazione @RequestMapping per configurare il componente.
@CrossOrigin // Applica l'annotazione @CrossOrigin per configurare il componente.
public class DipendenteController { // Dichiara la classe DipendenteController che incapsula la logica del dominio.

    private final DipendenteService service; // Mantiene il riferimento al servizio applicativo DipendenteService per delegare le operazioni di business.

    public DipendenteController(DipendenteService service) { // Costruttore della classe DipendenteController che inizializza le dipendenze richieste.
        this.service = service; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Dipendente> getAll() { return service.findAll(); } // Restituisce la lista completa degli elementi gestiti dal sistema.

    @GetMapping("/{id}") // Applica l'annotazione @GetMapping per configurare il componente.
    public Dipendente getById(@PathVariable Long id) { return service.findById(id); } // Restituisce il by id dell'entità.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Dipendente create(@RequestBody Dipendente dipendente) { return service.save(dipendente); } // Metodo create che gestisce la logica prevista.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public Dipendente update(@PathVariable Long id, @RequestBody Dipendente dipendente) { // Aggiorna l'entità applicando i dati forniti.
        return service.update(id, dipendente); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public void delete(@PathVariable Long id) { service.delete(id); } // Elimina l'entità identificata dal parametro in ingresso.
} // Chiude il blocco di codice precedente.
