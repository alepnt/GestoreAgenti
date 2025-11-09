package com.example.GestoreAgenti.service.facade; // Definisce il pacchetto com.example.GestoreAgenti.service.facade che contiene questa classe.

import java.util.ArrayList; // Importa java.util.ArrayList per abilitare le funzionalità utilizzate nel file.
import java.util.LinkedHashSet; // Importa java.util.LinkedHashSet per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Objects; // Importa java.util.Objects per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.
import java.util.stream.Collectors; // Importa java.util.stream.Collectors per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Provvigione; // Importa com.example.GestoreAgenti.model.Provvigione per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Team; // Importa com.example.GestoreAgenti.model.Team per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.composite.TeamComponent; // Importa com.example.GestoreAgenti.model.composite.TeamComponent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.ContrattoService; // Importa com.example.GestoreAgenti.service.ContrattoService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.DipendenteService; // Importa com.example.GestoreAgenti.service.DipendenteService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.ProvvigioneService; // Importa com.example.GestoreAgenti.service.ProvvigioneService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.TeamService; // Importa com.example.GestoreAgenti.service.TeamService per abilitare le funzionalità utilizzate nel file.

/**
 * Facade che coordina i servizi di dominio relativi agli agenti, aggregando i
 * dati provenienti da repository diversi e offrendo un'API più espressiva per i
 * controller REST.
 */
@Service("agentDomainFacade") // Applica l'annotazione @Service per configurare il componente.
public class AgentDomainFacade implements AgentOperations { // Definisce la classe AgentDomainFacade che incapsula la logica applicativa.

    private final DipendenteService dipendenteService; // Dichiara il campo dipendenteService dell'oggetto.
    private final ContrattoService contrattoService; // Dichiara il campo contrattoService dell'oggetto.
    private final ProvvigioneService provvigioneService; // Dichiara il campo provvigioneService dell'oggetto.
    private final TeamService teamService; // Dichiara il campo teamService dell'oggetto.
    private final TeamHierarchyBuilder hierarchyBuilder; // Dichiara il campo hierarchyBuilder dell'oggetto.

    public AgentDomainFacade(DipendenteService dipendenteService, // Costruttore della classe AgentDomainFacade che inizializza le dipendenze necessarie.
                             ContrattoService contrattoService, // Esegue l'istruzione necessaria alla logica applicativa.
                             ProvvigioneService provvigioneService, // Esegue l'istruzione necessaria alla logica applicativa.
                             TeamService teamService, // Esegue l'istruzione necessaria alla logica applicativa.
                             TeamHierarchyBuilder hierarchyBuilder) { // Apre il blocco di codice associato alla dichiarazione.
        this.dipendenteService = dipendenteService; // Aggiorna il campo dipendenteService dell'istanza.
        this.contrattoService = contrattoService; // Aggiorna il campo contrattoService dell'istanza.
        this.provvigioneService = provvigioneService; // Aggiorna il campo provvigioneService dell'istanza.
        this.teamService = teamService; // Aggiorna il campo teamService dell'istanza.
        this.hierarchyBuilder = hierarchyBuilder; // Aggiorna il campo hierarchyBuilder dell'istanza.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public List<Dipendente> listAgents() { // Definisce il metodo listAgents che supporta la logica di dominio.
        return dipendenteService.findAll(); // Restituisce il risultato dell'espressione dipendenteService.findAll().
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente getAgent(Long id) { // Definisce il metodo getAgent che supporta la logica di dominio.
        return dipendenteService.findById(id); // Restituisce il risultato dell'espressione dipendenteService.findById(id).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente registerAgent(Dipendente dipendente) { // Definisce il metodo registerAgent che supporta la logica di dominio.
        return dipendenteService.save(dipendente); // Restituisce il risultato dell'espressione dipendenteService.save(dipendente).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente updateAgent(Long id, Dipendente dipendente) { // Definisce il metodo updateAgent che supporta la logica di dominio.
        return dipendenteService.update(id, dipendente); // Restituisce il risultato dell'espressione dipendenteService.update(id, dipendente).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void removeAgent(Long id) { // Definisce il metodo removeAgent che supporta la logica di dominio.
        dipendenteService.delete(id); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Optional<AgentOverview> loadOverview(Long id) { // Definisce il metodo loadOverview che supporta la logica di dominio.
        Dipendente agent = getAgent(id); // Assegna il valore calcolato alla variabile Dipendente agent.
        if (agent == null) { // Valuta la condizione per controllare il flusso applicativo.
            return Optional.empty(); // Restituisce il risultato dell'espressione Optional.empty().
        } // Chiude il blocco di codice precedente.
        List<Contratto> contratti = contrattoService.getContrattiByDipendente(id); // Assegna il valore calcolato alla variabile List<Contratto> contratti.
        List<Provvigione> provvigioni = provvigioneService.getProvvigioniByDipendente(id); // Assegna il valore calcolato alla variabile List<Provvigione> provvigioni.
        List<Cliente> clienti = contratti.stream() // Esegue l'istruzione necessaria alla logica applicativa.
                .map(Contratto::getCliente) // Esegue l'istruzione necessaria alla logica applicativa.
                .filter(Objects::nonNull) // Esegue l'istruzione necessaria alla logica applicativa.
                .collect(Collectors.collectingAndThen( // Esegue l'istruzione necessaria alla logica applicativa.
                        Collectors.toCollection(LinkedHashSet::new), // Esegue l'istruzione necessaria alla logica applicativa.
                        ArrayList::new)); // Esegue l'istruzione terminata dal punto e virgola.
        Team assignedTeam = teamService.findAll().stream() // Esegue l'istruzione necessaria alla logica applicativa.
                .filter(team -> Objects.equals(team.getResponsabileId(), agent.getId()) // Esegue l'istruzione necessaria alla logica applicativa.
                        || Objects.equals(team.getProvincia(), agent.getTeam())) // Esegue l'istruzione necessaria alla logica applicativa.
                .findFirst() // Esegue l'istruzione necessaria alla logica applicativa.
                .orElse(null); // Esegue l'istruzione terminata dal punto e virgola.
        return Optional.of(new AgentOverview(agent, clienti, contratti, provvigioni, assignedTeam)); // Restituisce il risultato dell'espressione Optional.of(new AgentOverview(agent, clienti, contratti, provvigioni, assignedTeam)).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente assignAgentToTeam(Long agentId, Long teamId) { // Definisce il metodo assignAgentToTeam che supporta la logica di dominio.
        Dipendente agent = dipendenteService.findById(agentId); // Assegna il valore calcolato alla variabile Dipendente agent.
        if (agent == null) { // Valuta la condizione per controllare il flusso applicativo.
            throw new IllegalArgumentException("Dipendente non trovato: " + agentId); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
        Team team = teamService.findById(teamId) // Esegue l'istruzione necessaria alla logica applicativa.
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato: " + teamId)); // Esegue l'istruzione terminata dal punto e virgola.
        agent.setTeam(team.getProvincia()); // Esegue l'istruzione terminata dal punto e virgola.
        return dipendenteService.update(agentId, agent); // Restituisce il risultato dell'espressione dipendenteService.update(agentId, agent).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public TeamHierarchyNode buildHierarchy() { // Definisce il metodo buildHierarchy che supporta la logica di dominio.
        TeamComponent root = hierarchyBuilder.buildHierarchy(teamService.findAll(), dipendenteService.findAll()); // Assegna il valore calcolato alla variabile TeamComponent root.
        return TeamHierarchyNode.fromComponent(root); // Restituisce il risultato dell'espressione TeamHierarchyNode.fromComponent(root).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
