package com.example.GestoreAgenti.security; // Definisce il pacchetto com.example.GestoreAgenti.security a cui appartiene questa classe.

import org.springframework.security.core.userdetails.User; // Importa User per rappresentare un utente di Spring Security.
import org.springframework.security.core.userdetails.UserDetails; // Importa UserDetails per manipolare le informazioni di autenticazione dell'utente.
import org.springframework.security.core.userdetails.UserDetailsService; // Importa UserDetailsService per fornire i dettagli degli utenti a Spring Security.
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Importa UsernameNotFoundException per segnalare utenti inesistenti.
import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Utente; // Importa la classe Utente per utilizzare i dati anagrafici degli utenti.
import com.example.GestoreAgenti.repository.UtenteRepository; // Importa UtenteRepository per recuperare e salvare le informazioni degli utenti.

@Service // Applica l'annotazione @Service per configurare il componente.
public class CustomUserDetailsService implements UserDetailsService { // Dichiara la classe CustomUserDetailsService che incapsula la logica del dominio.

    private final UtenteRepository repository; // Mantiene il riferimento al repository UtenteRepository per accedere ai dati persistenti.

    public CustomUserDetailsService(UtenteRepository repository) { // Costruttore della classe CustomUserDetailsService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Recupera i dettagli dell'utente a partire dallo username.
        Utente utente = repository.findByUsername(username) // Inietta il repository per accedere ai dati persistenti.
                .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato")); // Elabora il risultato opzionale scegliendo il comportamento appropriato.

        return User.builder() // Restituisce il risultato dell'elaborazione al chiamante.
                .username(utente.getUsername()) // Imposta il nome utente nel builder dell'utente autenticato.
                .password(utente.getPasswordHash()) // Imposta l'hash della password nel builder dell'utente autenticato.
                .roles(utente.getRuolo().name()) // 👈 fondamentale per Autorizzazioni // Assegna i ruoli autorizzativi all'utente costruito.
                .build(); // Finalizza il parser JWT configurato.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
