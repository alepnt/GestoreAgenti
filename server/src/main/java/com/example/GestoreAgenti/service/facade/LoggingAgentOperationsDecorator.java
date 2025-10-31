package com.example.GestoreAgenti.service.facade;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Dipendente;

/**
 * Decorator che aggiunge log strutturato alle operazioni della facciata.
 */
@Service
@Primary
public class LoggingAgentOperationsDecorator implements AgentOperations {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAgentOperationsDecorator.class);

    private final AgentOperations delegate;

    public LoggingAgentOperationsDecorator(@Qualifier("agentDomainFacade") AgentOperations delegate) {
        this.delegate = delegate;
    }

    @Override
    public List<Dipendente> listAgents() {
        LOGGER.debug("Richiesta elenco agenti");
        return delegate.listAgents();
    }

    @Override
    public Dipendente getAgent(Long id) {
        LOGGER.debug("Recupero agente con id {}", id);
        return delegate.getAgent(id);
    }

    @Override
    public Dipendente registerAgent(Dipendente dipendente) {
        LOGGER.info("Registrazione nuovo agente {} {}", dipendente.getNome(), dipendente.getCognome());
        return delegate.registerAgent(dipendente);
    }

    @Override
    public Dipendente updateAgent(Long id, Dipendente dipendente) {
        LOGGER.info("Aggiornamento agente {}", id);
        return delegate.updateAgent(id, dipendente);
    }

    @Override
    public void removeAgent(Long id) {
        LOGGER.warn("Eliminazione agente {}", id);
        delegate.removeAgent(id);
    }

    @Override
    public Optional<AgentOverview> loadOverview(Long id) {
        LOGGER.debug("Richiesta overview agente {}", id);
        return delegate.loadOverview(id);
    }

    @Override
    public Dipendente assignAgentToTeam(Long agentId, Long teamId) {
        LOGGER.info("Assegnazione agente {} al team {}", agentId, teamId);
        return delegate.assignAgentToTeam(agentId, teamId);
    }

    @Override
    public TeamHierarchyNode buildHierarchy() {
        LOGGER.debug("Costruzione gerarchia team");
        return delegate.buildHierarchy();
    }
}
