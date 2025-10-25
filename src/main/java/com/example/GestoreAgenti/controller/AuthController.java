package com.example.GestoreAgenti.controller; // Definisce il pacchetto com.example.GestoreAgenti.controller a cui appartiene questa classe.

import org.springframework.security.authentication.AuthenticationManager; // Importa AuthenticationManager per orchestrare il processo di autenticazione.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Importa UsernamePasswordAuthenticationToken per incapsulare le credenziali dell'utente.
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails per manipolare le informazioni di autenticazione dell'utente.
import org.springframework.web.bind.annotation.PostMapping; // Importa PostMapping per mappare le richieste HTTP POST.
import org.springframework.web.bind.annotation.RequestBody; // Importa RequestBody per deserializzare il corpo della richiesta.
import org.springframework.web.bind.annotation.RequestMapping; // Importa RequestMapping per definire il prefisso delle rotte del controller.
import org.springframework.web.bind.annotation.RestController; // Importa RestController per esporre il controller come componente REST.

import com.example.GestoreAgenti.security.CustomUserDetailsService; // Importa CustomUserDetailsService per caricare i dettagli dell'utente durante l'autenticazione.
import com.example.GestoreAgenti.security.JwtUtil; // Importa JwtUtil per creare e verificare i token JWT.

@RestController // Applica l'annotazione @RestController per configurare il componente.
@RequestMapping("/api/auth") // Applica l'annotazione @RequestMapping per configurare il componente.
public class AuthController { // Dichiara la classe AuthController che incapsula la logica del dominio.

    private final AuthenticationManager authManager; // Memorizza l'authentication manager associato all'entità.
    private final CustomUserDetailsService userDetailsService; // Mantiene il riferimento al servizio applicativo CustomUserDetailsService per delegare le operazioni di business.
    private final JwtUtil jwtUtil; // Mantiene il riferimento all'utilità JwtUtil per le operazioni condivise.

    public AuthController(AuthenticationManager authManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) { // Costruttore della classe AuthController che inizializza le dipendenze richieste.
        this.authManager = authManager; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.userDetailsService = userDetailsService; // Aggiorna il campo dell'istanza con il valore ricevuto.
        this.jwtUtil = jwtUtil; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public static class LoginRequest { // Dichiara la classe LoginRequest che incapsula la logica del dominio.
        public String username; // Memorizza il nome utente dell'entità.
        public String password; // Memorizza la password dell'entità.
    } // Chiude il blocco di codice precedente.

    @PostMapping("/login") // Applica l'annotazione @PostMapping per configurare il componente.
    public String login(@RequestBody LoginRequest request) { // Metodo login che gestisce la logica prevista.
        authManager.authenticate( // Richiede al gestore di autenticazione di validare le credenziali fornite.
                new UsernamePasswordAuthenticationToken(request.username, request.password)); // Istanzia il token di autenticazione con le credenziali e le autorità dell'utente.

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username); // Recupera i dettagli dell'utente dal servizio di sicurezza.
        return jwtUtil.generateToken(userDetails.getUsername()); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

