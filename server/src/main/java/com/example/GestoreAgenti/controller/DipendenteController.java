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

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.GestoreAgenti.model.Dipendente; // Importa la classe Dipendente per operare sui dati dei dipendenti.
import com.example.GestoreAgenti.service.facade.AgentOperations;
import com.example.GestoreAgenti.service.facade.AgentOverview;
import com.example.GestoreAgenti.service.facade.TeamHierarchyNode;

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/dipendenti") // Applica l'annotazione @RequestMapping per configurare il componente.
@CrossOrigin // Applica l'annotazione @CrossOrigin per configurare il componente.
public class DipendenteController { // Dichiara la classe DipendenteController che incapsula la logica del dominio.

    private final AgentOperations agentOperations; // Mantiene il riferimento alla facciata decorata per ridurre le dipendenze dirette dai servizi atomici.

    public DipendenteController(AgentOperations agentOperations) { // Costruttore della classe DipendenteController che inizializza le dipendenze richieste.
        this.agentOperations = agentOperations; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @GetMapping // Applica l'annotazione @GetMapping per configurare il componente.
    public List<Dipendente> getAll() { return agentOperations.listAgents(); } // Restituisce la lista completa degli elementi gestiti dal sistema.

    @GetMapping("/{id}") // Applica l'annotazione @GetMapping per configurare il componente.
    public Dipendente getById(@PathVariable Long id) { // Restituisce il by id dell'entità.
        Dipendente dipendente = agentOperations.getAgent(id);
        if (dipendente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dipendente non trovato");
        }
        return dipendente;
    }

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Dipendente create(@RequestBody Dipendente dipendente) { return agentOperations.registerAgent(dipendente); } // Metodo create che gestisce la logica prevista.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public Dipendente update(@PathVariable Long id, @RequestBody Dipendente dipendente) { // Aggiorna l'entità applicando i dati forniti.
        return agentOperations.updateAgent(id, dipendente); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public void delete(@PathVariable Long id) { agentOperations.removeAgent(id); } // Elimina l'entità identificata dal parametro in ingresso.

    @GetMapping("/{id}/overview")
    public AgentOverview getOverview(@PathVariable Long id) {
        return agentOperations.loadOverview(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Overview non disponibile"));
    }

    @PostMapping("/{id}/teams/{teamId}")
    public Dipendente assignTeam(@PathVariable Long id, @PathVariable Long teamId) {
        try {
            return agentOperations.assignAgentToTeam(id, teamId);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/gerarchia")
    public TeamHierarchyNode getHierarchy() {
        return agentOperations.buildHierarchy();
    }
} // Chiude il blocco di codice precedente.
