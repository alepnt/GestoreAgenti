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

import com.example.GestoreAgenti.model.Team; // Importa la classe Team per manipolare i team gestiti dall'applicazione.
import com.example.GestoreAgenti.service.TeamService; // Importa TeamService per applicare la logica di business dei team.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/team") // Applica l'annotazione @RequestMapping per configurare il componente.
public class TeamController { // Dichiara la classe TeamController che incapsula la logica del dominio.

    private final TeamService service; // Mantiene il riferimento al servizio applicativo TeamService per delegare le operazioni di business.

    public TeamController(TeamService service) { // Costruttore della classe TeamController che inizializza le dipendenze richieste.
        this.service = service; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Team> getAllTeams() { // Restituisce la lista di il teams gestiti dal sistema.
        return service.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @GetMapping("/{id}") // Applica l'annotazione @GetMapping per configurare il componente.
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) { // Restituisce i dati di team filtrati in base a ID.
        return service.findById(id) // Restituisce il risultato dell'elaborazione al chiamante.
                .map(ResponseEntity::ok) // Gestisce la risposta HTTP per l'endpoint REST.
                .orElse(ResponseEntity.notFound().build()); // Gestisce la risposta HTTP per l'endpoint REST.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Team createTeam(@RequestBody Team team) { // Metodo create team che gestisce la logica prevista.
        return service.save(team); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) { // Aggiorna il team applicando i dati forniti.
        try { // Apre il blocco di istruzioni relativo alla dichiarazione precedente.
            Team updated = service.update(id, team); // Inietta il servizio applicativo necessario all'operazione.
            return ResponseEntity.ok(updated); // Restituisce il risultato dell'elaborazione al chiamante.
        } catch (RuntimeException e) { // Apre il blocco di istruzioni relativo alla dichiarazione precedente.
            return ResponseEntity.notFound().build(); // Restituisce il risultato dell'elaborazione al chiamante.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) { // Elimina il team identificato dall'input.
        service.delete(id); // Esegue questa istruzione come parte della logica del metodo.
        return ResponseEntity.noContent().build(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

