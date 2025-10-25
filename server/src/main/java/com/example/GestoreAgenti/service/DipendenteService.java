package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Dipendente; // Importa la classe Dipendente per operare sui dati dei dipendenti.
import com.example.GestoreAgenti.repository.DipendenteRepository; // Importa DipendenteRepository per interagire con la tabella dei dipendenti.

@Service // Applica l'annotazione @Service per configurare il componente.
public class DipendenteService { // Dichiara la classe DipendenteService che incapsula la logica del dominio.

    private final DipendenteRepository repository; // Mantiene il riferimento al repository DipendenteRepository per accedere ai dati persistenti.

    public DipendenteService(DipendenteRepository repository) { // Costruttore della classe DipendenteService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Dipendente> findAll() { return repository.findAll(); } // Restituisce tutti gli elementi gestiti dal servizio.

    public Dipendente findById(Long id) { return repository.findById(id).orElse(null); } // Ricerca l'entità in base a ID.

    public Dipendente save(Dipendente dipendente) { return repository.save(dipendente); } // Salva l'entità persistendola nel database.

    public Dipendente update(Long id, Dipendente nuovo) { // Aggiorna l'entità applicando i dati forniti.
        Dipendente esistente = findById(id); // Esegue questa istruzione come parte della logica del metodo.
        if (esistente == null) return null; // Valuta la condizione per determinare il ramo di esecuzione.
        nuovo.setId(id); // Esegue questa istruzione come parte della logica del metodo.
        return repository.save(nuovo); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public void delete(Long id) { repository.deleteById(id); } // Elimina l'entità identificata dal parametro in ingresso.
} // Chiude il blocco di codice precedente.
