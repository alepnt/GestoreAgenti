package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Importa BCryptPasswordEncoder per cifrare le password degli utenti.
import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Utente; // Importa la classe Utente per utilizzare i dati anagrafici degli utenti.
import com.example.GestoreAgenti.repository.UtenteRepository; // Importa UtenteRepository per recuperare e salvare le informazioni degli utenti.

@Service // Applica l'annotazione @Service per configurare il componente.
public class UtenteService { // Dichiara la classe UtenteService che incapsula la logica del dominio.

    private final UtenteRepository repository; // Mantiene il riferimento al repository UtenteRepository per accedere ai dati persistenti.
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // Memorizza il b crypt password encoder associato all'entità.

    public UtenteService(UtenteRepository repository) { // Costruttore della classe UtenteService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Utente> getAllUtenti() { // Restituisce la lista di i utenti gestiti dal sistema.
        return repository.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Optional<Utente> getUtenteById(Long id) { // Restituisce i dati di utente filtrati in base a ID.
        return repository.findById(id); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Utente createUtente(Utente utente) { // Metodo create utente che gestisce la logica prevista.
        utente.setPasswordHash(passwordEncoder.encode(utente.getPasswordHash())); // Esegue questa istruzione come parte della logica del metodo.
        return repository.save(utente); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Utente updateUtente(Long id, Utente utenteDetails) { // Aggiorna le utente applicando i dati forniti.
        return repository.findById(id).map(utente -> { // Restituisce il risultato dell'elaborazione al chiamante.
            utente.setUsername(utenteDetails.getUsername()); // Esegue questa istruzione come parte della logica del metodo.
            if (utenteDetails.getPasswordHash() != null && !utenteDetails.getPasswordHash().isEmpty()) { // Valuta la condizione per determinare il ramo di esecuzione.
                utente.setPasswordHash(passwordEncoder.encode(utenteDetails.getPasswordHash())); // Esegue questa istruzione come parte della logica del metodo.
            } // Chiude il blocco di codice precedente.
            utente.setRuolo(utenteDetails.getRuolo()); // Esegue questa istruzione come parte della logica del metodo.
            utente.setDipendente(utenteDetails.getDipendente()); // Esegue questa istruzione come parte della logica del metodo.
            return repository.save(utente); // Restituisce il risultato dell'elaborazione al chiamante.
        }).orElseThrow(() -> new RuntimeException("Utente non trovato con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void deleteUtente(Long id) { // Elimina le utente identificato dall'input.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

