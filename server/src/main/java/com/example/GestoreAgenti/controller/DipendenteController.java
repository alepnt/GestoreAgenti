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

import org.springframework.http.HttpStatus; // Importa org.springframework.http.HttpStatus per abilitare le funzionalità utilizzate nel file.
import org.springframework.web.server.ResponseStatusException; // Importa org.springframework.web.server.ResponseStatusException per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa la classe Dipendente per operare sui dati dei dipendenti.
import com.example.GestoreAgenti.service.facade.AgentOperations; // Importa com.example.GestoreAgenti.service.facade.AgentOperations per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.facade.AgentOverview; // Importa com.example.GestoreAgenti.service.facade.AgentOverview per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.facade.TeamHierarchyNode; // Importa com.example.GestoreAgenti.service.facade.TeamHierarchyNode per abilitare le funzionalità utilizzate nel file.

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
        Dipendente dipendente = agentOperations.getAgent(id); // Assegna il valore calcolato alla variabile Dipendente dipendente.
        if (dipendente == null) { // Valuta la condizione per controllare il flusso applicativo.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dipendente non trovato"); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        return dipendente; // Restituisce il risultato dell'espressione dipendente.
    } // Chiude il blocco di codice precedente.

    @PostMapping // Applica l'annotazione @PostMapping per configurare il componente.
    public Dipendente create(@RequestBody Dipendente dipendente) { return agentOperations.registerAgent(dipendente); } // Metodo create che gestisce la logica prevista.

    @PutMapping("/{id}") // Applica l'annotazione @PutMapping per configurare il componente.
    public Dipendente update(@PathVariable Long id, @RequestBody Dipendente dipendente) { // Aggiorna l'entità applicando i dati forniti.
        return agentOperations.updateAgent(id, dipendente); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @DeleteMapping("/{id}") // Applica l'annotazione @DeleteMapping per configurare il componente.
    public void delete(@PathVariable Long id) { agentOperations.removeAgent(id); } // Elimina l'entità identificata dal parametro in ingresso.

    @GetMapping("/{id}/overview") // Applica l'annotazione @GetMapping per configurare il componente.
    public AgentOverview getOverview(@PathVariable Long id) { // Definisce il metodo getOverview che supporta la logica di dominio.
        return agentOperations.loadOverview(id) // Restituisce il risultato dell'espressione agentOperations.loadOverview(id).
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Overview non disponibile")); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @PostMapping("/{id}/teams/{teamId}") // Applica l'annotazione @PostMapping per configurare il componente.
    public Dipendente assignTeam(@PathVariable Long id, @PathVariable Long teamId) { // Definisce il metodo assignTeam che supporta la logica di dominio.
        try { // Avvia il blocco protetto per intercettare eventuali eccezioni.
            return agentOperations.assignAgentToTeam(id, teamId); // Restituisce il risultato dell'espressione agentOperations.assignAgentToTeam(id, teamId).
        } catch (IllegalArgumentException ex) { // Apre il blocco di codice associato alla dichiarazione.
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    @GetMapping("/gerarchia") // Applica l'annotazione @GetMapping per configurare il componente.
    public TeamHierarchyNode getHierarchy() { // Definisce il metodo getHierarchy che supporta la logica di dominio.
        return agentOperations.buildHierarchy(); // Restituisce il risultato dell'espressione agentOperations.buildHierarchy().
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
