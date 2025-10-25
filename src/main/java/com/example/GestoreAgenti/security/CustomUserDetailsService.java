package com.example.GestoreAgenti.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Utente;
import com.example.GestoreAgenti.repository.UtenteRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtenteRepository repository;

    public CustomUserDetailsService(UtenteRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utente utente = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato"));

        return User.builder()
                .username(utente.getUsername())
                .password(utente.getPasswordHash())
                .roles(utente.getRuolo()) // 👈 fondamentale per Autorizzazioni
                .build();
    }
}
