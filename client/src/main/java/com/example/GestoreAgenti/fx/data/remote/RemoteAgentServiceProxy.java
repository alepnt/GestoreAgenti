package com.example.GestoreAgenti.fx.data.remote;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

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
        this.delegate = Objects.requireNonNull(delegate, "delegate");
        this.cacheDuration = Objects.requireNonNull(cacheDuration, "cacheDuration");
    }

    @Override
    public synchronized List<EmployeeDto> fetchAgents(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            throw new IllegalArgumentException("Token di autenticazione mancante");
        }
        Instant now = Instant.now();
        if (isCacheValid(now)) {
            return cache;
        }
        List<EmployeeDto> result = delegate.fetchAgents(authToken);
        cache = List.copyOf(result);
        lastRefresh = now;
        return cache;
    }

    private boolean isCacheValid(Instant now) {
        return lastRefresh != null
                && Duration.between(lastRefresh, now).compareTo(cacheDuration) < 0;
    }
}
