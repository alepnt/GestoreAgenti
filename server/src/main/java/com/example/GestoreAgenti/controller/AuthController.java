package com.example.GestoreAgenti.controller;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.GestoreAgenti.security.CustomUserDetailsService;
import com.example.GestoreAgenti.security.JwtUtil;

/**
 * Gestisce il processo di autenticazione generando token JWT per gli utenti
 * validati.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Payload di login trasmesso dal client.
     */
    public static class LoginRequest {
        public String username;
        public String password;
    }

    /** Autentica l'utente e restituisce un token JWT firmato. */
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username, request.password));

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username);
        return jwtUtil.generateToken(userDetails.getUsername());
    }
}

