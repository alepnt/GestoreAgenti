package com.example.GestoreAgenti.fx.data.remote;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class RemoteAgentServiceProxyTest {

    @Test
    void fetchAgentsRejectsMissingToken() {
        RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(token -> List.of());
        assertThrows(IllegalArgumentException.class, () -> proxy.fetchAgents(" "));
    }

    @Test
    void cachePreventsRepeatedCallsWithinDuration() {
        AtomicInteger calls = new AtomicInteger();
        RemoteAgentService delegate = token -> {
            calls.incrementAndGet();
            return List.of(new EmployeeDto("1", "Mario", "Rossi", "Ruolo", "Team", "mail"));
        };
        RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(delegate, Duration.ofMinutes(5));

        List<EmployeeDto> first = proxy.fetchAgents("token");
        List<EmployeeDto> second = proxy.fetchAgents("token");

        assertEquals(1, calls.get(), "Delegate should be called only once within cache duration");
        assertEquals(first, second);
    }

    @Test
    void cacheExpiresAfterDuration() throws Exception {
        RecordingService delegate = new RecordingService();
        RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(delegate, Duration.ofMillis(10));

        proxy.fetchAgents("token");

        var lastRefreshField = RemoteAgentServiceProxy.class.getDeclaredField("lastRefresh");
        lastRefreshField.setAccessible(true);
        lastRefreshField.set(proxy, Instant.now().minusMillis(100));

        proxy.fetchAgents("token");

        assertEquals(2, delegate.calls);
    }

    private static final class RecordingService implements RemoteAgentService {
        private int calls;
        private final List<EmployeeDto> response = new ArrayList<>();

        private RecordingService() {
            response.add(new EmployeeDto("1", "A", "B", null, null, null));
        }

        @Override
        public List<EmployeeDto> fetchAgents(String authToken) {
            calls++;
            return response;
        }
    }
}
