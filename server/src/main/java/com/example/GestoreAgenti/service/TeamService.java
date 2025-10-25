package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.


import com.example.GestoreAgenti.model.Team; // Importa la classe Team per manipolare i team gestiti dall'applicazione.
import com.example.GestoreAgenti.repository.TeamRepository; // Importa TeamRepository per accedere ai dati dei team.
import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

@Service // Applica l'annotazione @Service per configurare il componente.
public class TeamService { // Dichiara la classe TeamService che incapsula la logica del dominio.

    private final TeamRepository repository; // Mantiene il riferimento al repository TeamRepository per accedere ai dati persistenti.

    public TeamService(TeamRepository repository) { // Costruttore della classe TeamService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Team> findAll() { // Restituisce tutti gli elementi gestiti dal servizio.
        return repository.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Optional<Team> findById(Long id) { // Ricerca l'entità in base a ID.
        return repository.findById(id); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Team save(Team team) { // Salva l'entità persistendola nel database.
        return repository.save(team); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Team update(Long id, Team updatedTeam) { // Aggiorna l'entità applicando i dati forniti.
        return repository.findById(id) // Restituisce il risultato dell'elaborazione al chiamante.
                .map(existing -> { // Elabora il risultato opzionale scegliendo il comportamento appropriato.
                    existing.setProvincia(updatedTeam.getProvincia()); // Esegue questa istruzione come parte della logica del metodo.
                    existing.setResponsabileId(updatedTeam.getResponsabileId()); // Esegue questa istruzione come parte della logica del metodo.
                    existing.setTotProfittoMensile(updatedTeam.getTotProfittoMensile()); // Esegue questa istruzione come parte della logica del metodo.
                    existing.setTotProvvigioneMensile(updatedTeam.getTotProvvigioneMensile()); // Esegue questa istruzione come parte della logica del metodo.
                    existing.setTotProvvigioneAnnuo(updatedTeam.getTotProvvigioneAnnuo()); // Esegue questa istruzione come parte della logica del metodo.
                    return repository.save(existing); // Restituisce il risultato dell'elaborazione al chiamante.
                }) // Conclude la trasformazione restituendo l'entità aggiornata.
                .orElseThrow(() -> new RuntimeException("Team non trovato con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void delete(Long id) { // Elimina l'entità identificata dal parametro in ingresso.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

