package com.example.GestoreAgenti.security; // Definisce il pacchetto com.example.GestoreAgenti.security a cui appartiene questa classe.

import org.springframework.context.annotation.Bean; // Importa Bean per esporre componenti gestiti dal container Spring.
import org.springframework.context.annotation.Configuration; // Importa Configuration per dichiarare la classe di configurazione Spring.
import org.springframework.security.authentication.AuthenticationManager; // Importa AuthenticationManager per orchestrare il processo di autenticazione.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Importa AuthenticationConfiguration per ottenere i componenti di autenticazione dal contesto.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importa HttpSecurity per personalizzare le regole di sicurezza HTTP.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa BCryptPasswordEncoder per cifrare le password degli utenti.
import org.springframework.security.web.SecurityFilterChain; // Importa SecurityFilterChain per definire la catena di filtri di sicurezza.

@Configuration // Applica l'annotazione @Configuration per configurare il componente.
public class SecurityConfig { // Dichiara la classe SecurityConfig che incapsula la logica del dominio.

    @Bean // Applica l'annotazione @Bean per configurare il componente.
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); } // Fornisce il bean BCryptPasswordEncoder per codificare le password.

    @Bean // Applica l'annotazione @Bean per configurare il componente.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { // Espone il bean AuthenticationManager configurato da Spring Security.
        return config.getAuthenticationManager(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    @Bean // Applica l'annotazione @Bean per configurare il componente.
public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception { // Configura la catena di filtri di sicurezza HTTP dell'applicazione.

    http.csrf(csrf -> csrf.disable()) // Disabilita la protezione CSRF per consentire richieste stateless.
        .authorizeHttpRequests(auth -> auth // Configura le regole di autorizzazione per le richieste HTTP.
            .requestMatchers("/api/auth/**").permitAll()  // Login libero // Permette l'accesso pubblico agli endpoint indicati.
            .requestMatchers("/api/contratti/**").hasAnyRole("ADMIN", "AGENTE") // Limita l'endpoint ai ruoli elencati per controllare l'accesso.
            .requestMatchers("/api/provvigioni/**").hasAnyRole("ADMIN", "AGENTE") // Limita l'endpoint ai ruoli elencati per controllare l'accesso.
            .requestMatchers("/api/fatture/**").hasAnyRole("ADMIN", "AGENTE", "CONTABILE") // Limita l'endpoint ai ruoli elencati per controllare l'accesso.
            .requestMatchers("/api/pagamenti/**").hasAnyRole("ADMIN", "CONTABILE") // Limita l'endpoint ai ruoli elencati per controllare l'accesso.
            .requestMatchers("/api/**").hasRole("ADMIN") // Concede l'accesso solo al ruolo specificato.
            .anyRequest().authenticated() // Richiede l'autenticazione per tutte le altre richieste.
        ) // Conclude la configurazione delle regole di autorizzazione definite sopra.
        .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class); // Inserisce il filtro JWT prima del filtro di autenticazione standard.

    return http.build(); // Restituisce il risultato dell'elaborazione al chiamante.
} // Chiude il blocco di codice precedente.

} // Chiude il blocco di codice precedente.
