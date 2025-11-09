package com.example.GestoreAgenti.service.facade; // Definisce il pacchetto com.example.GestoreAgenti.service.facade che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.slf4j.Logger; // Importa org.slf4j.Logger per abilitare le funzionalità utilizzate nel file.
import org.slf4j.LoggerFactory; // Importa org.slf4j.LoggerFactory per abilitare le funzionalità utilizzate nel file.
import org.springframework.beans.factory.annotation.Qualifier; // Importa org.springframework.beans.factory.annotation.Qualifier per abilitare le funzionalità utilizzate nel file.
import org.springframework.context.annotation.Primary; // Importa org.springframework.context.annotation.Primary per abilitare le funzionalità utilizzate nel file.
import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.

/**
 * Decorator che aggiunge log strutturato alle operazioni della facciata.
 */
@Service // Applica l'annotazione @Service per configurare il componente.
@Primary // Applica l'annotazione @Primary per configurare il componente.
public class LoggingAgentOperationsDecorator implements AgentOperations { // Definisce la classe LoggingAgentOperationsDecorator che incapsula la logica applicativa.

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAgentOperationsDecorator.class); // Definisce il metodo LoggerFactory.getLogger che supporta la logica di dominio.

    private final AgentOperations delegate; // Dichiara il campo delegate dell'oggetto.

    public LoggingAgentOperationsDecorator(@Qualifier("agentDomainFacade") AgentOperations delegate) { // Costruttore della classe LoggingAgentOperationsDecorator che inizializza le dipendenze necessarie.
        this.delegate = delegate; // Aggiorna il campo delegate dell'istanza.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public List<Dipendente> listAgents() { // Definisce il metodo listAgents che supporta la logica di dominio.
        LOGGER.debug("Richiesta elenco agenti"); // Esegue l'istruzione terminata dal punto e virgola.
        return delegate.listAgents(); // Restituisce il risultato dell'espressione delegate.listAgents().
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente getAgent(Long id) { // Definisce il metodo getAgent che supporta la logica di dominio.
        LOGGER.debug("Recupero agente con id {}", id); // Esegue l'istruzione terminata dal punto e virgola.
        return delegate.getAgent(id); // Restituisce il risultato dell'espressione delegate.getAgent(id).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente registerAgent(Dipendente dipendente) { // Definisce il metodo registerAgent che supporta la logica di dominio.
        LOGGER.info("Registrazione nuovo agente {} {}", dipendente.getNome(), dipendente.getCognome()); // Esegue l'istruzione terminata dal punto e virgola.
        return delegate.registerAgent(dipendente); // Restituisce il risultato dell'espressione delegate.registerAgent(dipendente).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente updateAgent(Long id, Dipendente dipendente) { // Definisce il metodo updateAgent che supporta la logica di dominio.
        LOGGER.info("Aggiornamento agente {}", id); // Esegue l'istruzione terminata dal punto e virgola.
        return delegate.updateAgent(id, dipendente); // Restituisce il risultato dell'espressione delegate.updateAgent(id, dipendente).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public void removeAgent(Long id) { // Definisce il metodo removeAgent che supporta la logica di dominio.
        LOGGER.warn("Eliminazione agente {}", id); // Esegue l'istruzione terminata dal punto e virgola.
        delegate.removeAgent(id); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Optional<AgentOverview> loadOverview(Long id) { // Definisce il metodo loadOverview che supporta la logica di dominio.
        LOGGER.debug("Richiesta overview agente {}", id); // Esegue l'istruzione terminata dal punto e virgola.
        return delegate.loadOverview(id); // Restituisce il risultato dell'espressione delegate.loadOverview(id).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public Dipendente assignAgentToTeam(Long agentId, Long teamId) { // Definisce il metodo assignAgentToTeam che supporta la logica di dominio.
        LOGGER.info("Assegnazione agente {} al team {}", agentId, teamId); // Esegue l'istruzione terminata dal punto e virgola.
        return delegate.assignAgentToTeam(agentId, teamId); // Restituisce il risultato dell'espressione delegate.assignAgentToTeam(agentId, teamId).
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public TeamHierarchyNode buildHierarchy() { // Definisce il metodo buildHierarchy che supporta la logica di dominio.
        LOGGER.debug("Costruzione gerarchia team"); // Esegue l'istruzione terminata dal punto e virgola.
        return delegate.buildHierarchy(); // Restituisce il risultato dell'espressione delegate.buildHierarchy().
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
