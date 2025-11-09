package com.example.GestoreAgenti.security; // Definisce il pacchetto com.example.GestoreAgenti.security che contiene questa classe.

import org.springframework.context.annotation.Bean; // Importa org.springframework.context.annotation.Bean per abilitare le funzionalità utilizzate nel file.
import org.springframework.context.annotation.Configuration; // Importa org.springframework.context.annotation.Configuration per abilitare le funzionalità utilizzate nel file.
import org.springframework.security.authentication.AuthenticationManager; // Importa org.springframework.security.authentication.AuthenticationManager per abilitare le funzionalità utilizzate nel file.
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // Importa org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder per abilitare le funzionalità utilizzate nel file.
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Importa org.springframework.security.config.annotation.web.builders.HttpSecurity per abilitare le funzionalità utilizzate nel file.
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Importa org.springframework.security.config.annotation.web.configuration.EnableWebSecurity per abilitare le funzionalità utilizzate nel file.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder per abilitare le funzionalità utilizzate nel file.
import org.springframework.security.web.SecurityFilterChain; // Importa org.springframework.security.web.SecurityFilterChain per abilitare le funzionalità utilizzate nel file.

/**
 * Configura la sicurezza Spring consentendo l'accesso libero alle API della demo.
 */
@Configuration // Applica l'annotazione @Configuration per configurare il componente.
@EnableWebSecurity // Applica l'annotazione @EnableWebSecurity per configurare il componente.
public class SecurityConfig { // Definisce la classe SecurityConfig che incapsula la logica applicativa.

    @Bean // Applica l'annotazione @Bean per configurare il componente.
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { // Definisce il metodo securityFilterChain che supporta la logica di dominio.
        http // Esegue l'istruzione necessaria alla logica applicativa.
                .csrf(csrf -> csrf.disable()) // Esegue l'istruzione necessaria alla logica applicativa.
                .authorizeHttpRequests(registry -> registry.anyRequest().permitAll()); // Esegue l'istruzione terminata dal punto e virgola.
        return http.build(); // Restituisce il risultato dell'espressione http.build().
    } // Chiude il blocco di codice precedente.

    @Bean // Applica l'annotazione @Bean per configurare il componente.
    public BCryptPasswordEncoder passwordEncoder() { // Definisce il metodo passwordEncoder che supporta la logica di dominio.
        return new BCryptPasswordEncoder(); // Restituisce il risultato dell'espressione new BCryptPasswordEncoder().
    } // Chiude il blocco di codice precedente.

    @Bean // Applica l'annotazione @Bean per configurare il componente.
    public AuthenticationManager authenticationManager(HttpSecurity http, // Definisce il metodo authenticationManager che supporta la logica di dominio.
            BCryptPasswordEncoder passwordEncoder, // Esegue l'istruzione necessaria alla logica applicativa.
            CustomUserDetailsService userDetailsService) throws Exception { // Apre il blocco di codice associato alla dichiarazione.
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class); // Assegna il valore calcolato alla variabile AuthenticationManagerBuilder authBuilder.
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder); // Esegue l'istruzione terminata dal punto e virgola.
        return authBuilder.build(); // Restituisce il risultato dell'espressione authBuilder.build().
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
