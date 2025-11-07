package com.example.GestoreAgenti.fx.data.remote; // Definisce il package relativo alla comunicazione remota del client.

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto; // Importa il tipo di dato trasferito dal servizio remoto.

import java.time.Duration; // Importa Duration per gestire la finestra temporale della cache.
import java.time.Instant; // Importa Instant per tracciare il momento dell'ultimo aggiornamento.
import java.util.List; // Importa List per mantenere la cache di agenti.
import java.util.Objects; // Importa Objects per validare le dipendenze obbligatorie.

/**
 * Proxy che aggiunge caching e controlli di sicurezza alle chiamate verso il
 * servizio remoto.
 */
public class RemoteAgentServiceProxy implements RemoteAgentService { // Implementa l'interfaccia remota aggiungendo logica aggiuntiva lato client.

    private final RemoteAgentService delegate; // Riferimento al servizio reale su cui delegare le chiamate.
    private final Duration cacheDuration; // Durata massima entro cui considerare valida la cache locale.
    private Instant lastRefresh; // Memorizza il timestamp dell'ultimo aggiornamento della cache.
    private List<EmployeeDto> cache = List.of(); // Mantiene la lista di agenti più recente.

    public RemoteAgentServiceProxy(RemoteAgentService delegate) { // Costruttore che usa una durata predefinita per la cache.
        this(delegate, Duration.ofMinutes(2)); // Inoltra al costruttore principale impostando 2 minuti.
    } // Esegue: }

    public RemoteAgentServiceProxy(RemoteAgentService delegate, Duration cacheDuration) { // Costruttore principale.
        this.delegate = Objects.requireNonNull(delegate, "delegate"); // Verifica che il servizio delegato sia presente.
        this.cacheDuration = Objects.requireNonNull(cacheDuration, "cacheDuration"); // Verifica che la durata della cache sia configurata.
    } // Esegue: }

    @Override // Indica che il metodo implementa quello dichiarato nell'interfaccia.
    public synchronized List<EmployeeDto> fetchAgents(String authToken) { // Recupera l'elenco agenti applicando caching e sicurezza.
        if (authToken == null || authToken.isBlank()) { // Controlla che il token sia presente e non vuoto.
            throw new IllegalArgumentException("Token di autenticazione mancante"); // In caso contrario blocca la chiamata con un errore.
        } // Esegue: }
        Instant now = Instant.now(); // Registra l'istante corrente per confrontarlo con la cache.
        if (isCacheValid(now)) { // Se la cache è ancora entro i limiti temporali...
            return cache; // ...restituisce direttamente i dati salvati.
        } // Esegue: }
        List<EmployeeDto> result = delegate.fetchAgents(authToken); // Altrimenti chiede al servizio remoto l'elenco aggiornato.
        cache = List.copyOf(result); // Copia immutabile dei dati per evitare modifiche involontarie.
        lastRefresh = now; // Aggiorna il timestamp dell'ultimo refresh.
        return cache; // Restituisce l'elenco aggiornato al chiamante.
    } // Esegue: }

    private boolean isCacheValid(Instant now) { // Valuta se la cache è ancora utilizzabile.
        return lastRefresh != null // È valida solo se è già stato effettuato almeno un refresh...
                && Duration.between(lastRefresh, now).compareTo(cacheDuration) < 0; // ...e il tempo trascorso è inferiore alla durata consentita.
    } // Esegue: }
} // Esegue: }
