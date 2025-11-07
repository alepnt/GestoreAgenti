package com.example.GestoreAgenti.fx.data.remote; // Esegue: package com.example.GestoreAgenti.fx.data.remote;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto; // Esegue: import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;
import org.junit.jupiter.api.Test; // Esegue: import org.junit.jupiter.api.Test;

import java.time.Duration; // Esegue: import java.time.Duration;
import java.time.Instant; // Esegue: import java.time.Instant;
import java.util.ArrayList; // Esegue: import java.util.ArrayList;
import java.util.List; // Esegue: import java.util.List;
import java.util.concurrent.atomic.AtomicInteger; // Esegue: import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*; // Esegue: import static org.junit.jupiter.api.Assertions.*;

class RemoteAgentServiceProxyTest { // Esegue: class RemoteAgentServiceProxyTest {

    @Test // Esegue: @Test
    void fetchAgentsRejectsMissingToken() { // Esegue: void fetchAgentsRejectsMissingToken() {
        RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(token -> List.of()); // Esegue: RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(token -> List.of());
        assertThrows(IllegalArgumentException.class, () -> proxy.fetchAgents(" ")); // Esegue: assertThrows(IllegalArgumentException.class, () -> proxy.fetchAgents(" "));
    } // Esegue: }

    @Test // Esegue: @Test
    void cachePreventsRepeatedCallsWithinDuration() { // Esegue: void cachePreventsRepeatedCallsWithinDuration() {
        AtomicInteger calls = new AtomicInteger(); // Esegue: AtomicInteger calls = new AtomicInteger();
        RemoteAgentService delegate = token -> { // Esegue: RemoteAgentService delegate = token -> {
            calls.incrementAndGet(); // Esegue: calls.incrementAndGet();
            return List.of(new EmployeeDto("1", "Mario", "Rossi", "Ruolo", "Team", "mail")); // Esegue: return List.of(new EmployeeDto("1", "Mario", "Rossi", "Ruolo", "Team", "mail"));
        }; // Esegue: };
        RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(delegate, Duration.ofMinutes(5)); // Esegue: RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(delegate, Duration.ofMinutes(5));

        List<EmployeeDto> first = proxy.fetchAgents("token"); // Esegue: List<EmployeeDto> first = proxy.fetchAgents("token");
        List<EmployeeDto> second = proxy.fetchAgents("token"); // Esegue: List<EmployeeDto> second = proxy.fetchAgents("token");

        assertEquals(1, calls.get(), "Delegate should be called only once within cache duration"); // Esegue: assertEquals(1, calls.get(), "Delegate should be called only once within cache duration");
        assertEquals(first, second); // Esegue: assertEquals(first, second);
    } // Esegue: }

    @Test // Esegue: @Test
    void cacheExpiresAfterDuration() throws Exception { // Esegue: void cacheExpiresAfterDuration() throws Exception {
        RecordingService delegate = new RecordingService(); // Esegue: RecordingService delegate = new RecordingService();
        RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(delegate, Duration.ofMillis(10)); // Esegue: RemoteAgentServiceProxy proxy = new RemoteAgentServiceProxy(delegate, Duration.ofMillis(10));

        proxy.fetchAgents("token"); // Esegue: proxy.fetchAgents("token");

        var lastRefreshField = RemoteAgentServiceProxy.class.getDeclaredField("lastRefresh"); // Esegue: var lastRefreshField = RemoteAgentServiceProxy.class.getDeclaredField("lastRefresh");
        lastRefreshField.setAccessible(true); // Esegue: lastRefreshField.setAccessible(true);
        lastRefreshField.set(proxy, Instant.now().minusMillis(100)); // Esegue: lastRefreshField.set(proxy, Instant.now().minusMillis(100));

        proxy.fetchAgents("token"); // Esegue: proxy.fetchAgents("token");

        assertEquals(2, delegate.calls); // Esegue: assertEquals(2, delegate.calls);
    } // Esegue: }

    private static final class RecordingService implements RemoteAgentService { // Esegue: private static final class RecordingService implements RemoteAgentService {
        private int calls; // Esegue: private int calls;
        private final List<EmployeeDto> response = new ArrayList<>(); // Esegue: private final List<EmployeeDto> response = new ArrayList<>();

        private RecordingService() { // Esegue: private RecordingService() {
            response.add(new EmployeeDto("1", "A", "B", null, null, null)); // Esegue: response.add(new EmployeeDto("1", "A", "B", null, null, null));
        } // Esegue: }

        @Override // Esegue: @Override
        public List<EmployeeDto> fetchAgents(String authToken) { // Esegue: public List<EmployeeDto> fetchAgents(String authToken) {
            calls++; // Esegue: calls++;
            return response; // Esegue: return response;
        } // Esegue: }
    } // Esegue: }
} // Esegue: }
