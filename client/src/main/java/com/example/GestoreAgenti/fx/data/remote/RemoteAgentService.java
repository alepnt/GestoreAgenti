package com.example.GestoreAgenti.fx.data.remote;

import java.util.List;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;

/**
 * Contratto per i servizi remoti che forniscono i dati degli agenti.
 */
public interface RemoteAgentService {

    /**
     * Recupera i dipendenti dal backend remoto.
     *
     * @param authToken token di autenticazione richiesto dal backend
     * @return elenco di DTO ottenuti dal servizio remoto
     */
    List<EmployeeDto> fetchAgents(String authToken);
}
