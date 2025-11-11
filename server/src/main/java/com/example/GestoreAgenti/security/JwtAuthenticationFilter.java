package com.example.GestoreAgenti.security; // Definisce il pacchetto com.example.GestoreAgenti.security a cui appartiene questa classe.

import java.io.IOException; // Importa IOException per segnalare errori di input/output nel filtro.

import org.springframework.lang.NonNull; // Importa org.springframework.lang.NonNull per rispettare i contratti del filtro padre.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa UsernamePasswordAuthenticationToken per incapsulare le credenziali dell'utente.
import org.springframework.security.core.context.SecurityContextHolder; // Importa SecurityContextHolder per accedere al contesto di sicurezza corrente.
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails per manipolare le informazioni di autenticazione dell'utente.
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Importa WebAuthenticationDetailsSource per arricchire i dettagli di autenticazione della richiesta.
import org.springframework.stereotype.Component; // Importa Component per registrare la classe come componente Spring.
import org.springframework.web.filter.OncePerRequestFilter; // Importa OncePerRequestFilter per eseguire un filtro una sola volta per richiesta.

import jakarta.servlet.FilterChain; // Importa FilterChain per proseguire l'elaborazione della richiesta nella catena dei filtri.
import jakarta.servlet.ServletException; // Importa ServletException per gestire eventuali errori durante il filtraggio della richiesta.
import jakarta.servlet.http.HttpServletRequest; // Importa HttpServletRequest per leggere i dettagli della richiesta HTTP.
import jakarta.servlet.http.HttpServletResponse; // Importa HttpServletResponse per manipolare la risposta HTTP generata.

@Component // Applica l'annotazione @Component per configurare il componente.
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Dichiara la classe JwtAuthenticationFilter che incapsula la logica del dominio.

    private final JwtUtil jwtUtil; // Mantiene il riferimento all'utilità JwtUtil per le operazioni condivise.
    private final CustomUserDetailsService userDetailsService; // Mantiene il riferimento al servizio applicativo CustomUserDetailsService per delegare le operazioni di business.

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) { // Costruttore della classe JwtAuthenticationFilter che inizializza le dipendenze richieste.
        this.jwtUtil = jwtUtil; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.userDetailsService = userDetailsService; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    protected void doFilterInternal( // Definisce il metodo principale del filtro che gestisce ogni richiesta.
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain // Elenca i parametri utilizzati per analizzare la richiesta e la risposta HTTP.
    ) throws ServletException, IOException { // Apre il blocco di istruzioni relativo alla dichiarazione precedente.

        String authHeader = request.getHeader("Authorization"); // Esegue questa istruzione come parte della logica del metodo.

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { // Valuta la condizione per determinare il ramo di esecuzione.
            filterChain.doFilter(request, response); // Propaga la richiesta al filtro successivo nella catena.
            return; // Conclude il metodo restituendo il controllo al chiamante.
        } // Chiude il blocco di codice precedente.

        String jwt = authHeader.substring(7); // Rimuove "Bearer " // Estrae il token JWT rimuovendo il prefisso Bearer dalla stringa di autorizzazione.
        String username = jwtUtil.extractUsername(jwt); // Utilizza la classe JwtUtil per creare o verificare i token JWT.

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) { // Valuta la condizione per determinare il ramo di esecuzione.

            UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Recupera i dettagli dell'utente dal servizio di sicurezza.

            UsernamePasswordAuthenticationToken authToken = // Prepara il contenitore di autenticazione con i privilegi dell'utente.
                    new UsernamePasswordAuthenticationToken( // Istanzia il token di autenticazione con le credenziali e le autorità dell'utente.
                            userDetails, null, userDetails.getAuthorities()); // Esegue questa istruzione come parte della logica del metodo.

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Crea una nuova istanza e la passa al metodo richiamato.

            SecurityContextHolder.getContext().setAuthentication(authToken); // Esegue questa istruzione come parte della logica del metodo.
        } // Chiude il blocco di codice precedente.

        filterChain.doFilter(request, response); // Propaga la richiesta al filtro successivo nella catena.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

