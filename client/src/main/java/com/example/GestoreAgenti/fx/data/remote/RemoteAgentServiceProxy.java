package com.example.GestoreAgenti.fx.data.remote;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;

/**
 * Proxy che aggiunge caching e controlli di sicurezza alle chiamate verso il
 * servizio remoto.
 */
public class RemoteAgentServiceProxy implements RemoteAgentService {

    private final RemoteAgentService delegate;
    private final Duration cacheDuration;
    private Instant lastRefresh;
    private List<EmployeeDto> cache = List.of();

    public RemoteAgentServiceProxy(RemoteAgentService delegate) {
        this(delegate, Duration.ofMinutes(2));
    }

    public RemoteAgentServiceProxy(RemoteAgentService delegate, Duration cacheDuration) {
        this.delegate = delegate;
        this.cacheDuration = cacheDuration;
    }

    @Override
    public synchronized List<EmployeeDto> fetchAgents(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            throw new IllegalArgumentException("Token di autenticazione mancante");
        }
        if (lastRefresh != null && Duration.between(lastRefresh, Instant.now()).compareTo(cacheDuration) < 0) {
            return new ArrayList<>(cache);
        }
        List<EmployeeDto> result = delegate.fetchAgents(authToken);
        cache = List.copyOf(result);
        lastRefresh = Instant.now();
        return new ArrayList<>(cache);
    }
}
