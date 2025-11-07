package com.example.GestoreAgenti.fx.data.remote; // Definisce il package che contiene le astrazioni verso i servizi remoti.

import java.util.List; // Importa List per restituire collezioni di DTO.

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto; // Importa il DTO degli agenti recuperato dal backend.

/**
 * Contratto per i servizi remoti che forniscono i dati degli agenti.
 */
public interface RemoteAgentService { // Interfaccia che descrive le operazioni disponibili sui servizi remoti.

    /**
     * Recupera i dipendenti dal backend remoto.
     *
     * @param authToken token di autenticazione richiesto dal backend
     * @return elenco di DTO ottenuti dal servizio remoto
     */
    List<EmployeeDto> fetchAgents(String authToken); // Metodo astratto che richiede un token e restituisce i dipendenti.
} // Esegue: }
