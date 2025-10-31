package com.example.GestoreAgenti.service.facade;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Provvigione;
import com.example.GestoreAgenti.model.Team;
import com.example.GestoreAgenti.model.composite.TeamComponent;
import com.example.GestoreAgenti.service.ContrattoService;
import com.example.GestoreAgenti.service.DipendenteService;
import com.example.GestoreAgenti.service.ProvvigioneService;
import com.example.GestoreAgenti.service.TeamService;

/**
 * Facade che coordina i servizi di dominio relativi agli agenti, aggregando i
 * dati provenienti da repository diversi e offrendo un'API più espressiva per i
 * controller REST.
 */
@Service("agentDomainFacade")
public class AgentDomainFacade implements AgentOperations {

    private final DipendenteService dipendenteService;
    private final ContrattoService contrattoService;
    private final ProvvigioneService provvigioneService;
    private final TeamService teamService;
    private final TeamHierarchyBuilder hierarchyBuilder;

    public AgentDomainFacade(DipendenteService dipendenteService,
                             ContrattoService contrattoService,
                             ProvvigioneService provvigioneService,
                             TeamService teamService,
                             TeamHierarchyBuilder hierarchyBuilder) {
        this.dipendenteService = dipendenteService;
        this.contrattoService = contrattoService;
        this.provvigioneService = provvigioneService;
        this.teamService = teamService;
        this.hierarchyBuilder = hierarchyBuilder;
    }

    @Override
    public List<Dipendente> listAgents() {
        return dipendenteService.findAll();
    }

    @Override
    public Dipendente getAgent(Long id) {
        return dipendenteService.findById(id);
    }

    @Override
    public Dipendente registerAgent(Dipendente dipendente) {
        return dipendenteService.save(dipendente);
    }

    @Override
    public Dipendente updateAgent(Long id, Dipendente dipendente) {
        return dipendenteService.update(id, dipendente);
    }

    @Override
    public void removeAgent(Long id) {
        dipendenteService.delete(id);
    }

    @Override
    public Optional<AgentOverview> loadOverview(Long id) {
        Dipendente agent = getAgent(id);
        if (agent == null) {
            return Optional.empty();
        }
        List<Contratto> contratti = contrattoService.getContrattiByDipendente(id);
        List<Provvigione> provvigioni = provvigioneService.getProvvigioniByDipendente(id);
        List<Cliente> clienti = contratti.stream()
                .map(Contratto::getCliente)
                .filter(Objects::nonNull)
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(LinkedHashSet::new),
                        ArrayList::new));
        Team assignedTeam = teamService.findAll().stream()
                .filter(team -> Objects.equals(team.getResponsabileId(), agent.getId())
                        || Objects.equals(team.getProvincia(), agent.getTeam()))
                .findFirst()
                .orElse(null);
        return Optional.of(new AgentOverview(agent, clienti, contratti, provvigioni, assignedTeam));
    }

    @Override
    public Dipendente assignAgentToTeam(Long agentId, Long teamId) {
        Dipendente agent = dipendenteService.findById(agentId);
        if (agent == null) {
            throw new IllegalArgumentException("Dipendente non trovato: " + agentId);
        }
        Team team = teamService.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato: " + teamId));
        agent.setTeam(team.getProvincia());
        return dipendenteService.update(agentId, agent);
    }

    @Override
    public TeamHierarchyNode buildHierarchy() {
        TeamComponent root = hierarchyBuilder.buildHierarchy(teamService.findAll(), dipendenteService.findAll());
        return TeamHierarchyNode.fromComponent(root);
    }
}
