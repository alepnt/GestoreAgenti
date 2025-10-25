package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Provvigione; // Importa la classe Provvigione per trattare le provvigioni associate ai contratti.
import com.example.GestoreAgenti.repository.ProvvigioneRepository; // Importa ProvvigioneRepository per gestire l'accesso alle provvigioni.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ProvvigioneService { // Dichiara la classe ProvvigioneService che incapsula la logica del dominio.

    private final ProvvigioneRepository repository; // Mantiene il riferimento al repository ProvvigioneRepository per accedere ai dati persistenti.

    public ProvvigioneService(ProvvigioneRepository repository) { // Costruttore della classe ProvvigioneService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Provvigione> getAllProvvigioni() { // Restituisce la lista di i provvigioni gestiti dal sistema.
        return repository.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Optional<Provvigione> getProvvigioneById(Long id) { // Restituisce i dati di provvigione filtrati in base a ID.
        return repository.findById(id); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Provvigione createProvvigione(Provvigione provvigione) { // Metodo create provvigione che gestisce la logica prevista.
        return repository.save(provvigione); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Provvigione updateProvvigione(Long id, Provvigione provvigioneDetails) { // Aggiorna le provvigione applicando i dati forniti.
        return repository.findById(id).map(provvigione -> { // Restituisce il risultato dell'elaborazione al chiamante.
            provvigione.setDipendente(provvigioneDetails.getDipendente()); // Esegue questa istruzione come parte della logica del metodo.
            provvigione.setContratto(provvigioneDetails.getContratto()); // Esegue questa istruzione come parte della logica del metodo.
            provvigione.setPercentuale(provvigioneDetails.getPercentuale()); // Esegue questa istruzione come parte della logica del metodo.
            provvigione.setImporto(provvigioneDetails.getImporto()); // Esegue questa istruzione come parte della logica del metodo.
            provvigione.setStato(provvigioneDetails.getStato()); // Esegue questa istruzione come parte della logica del metodo.
            provvigione.setDataCalcolo(provvigioneDetails.getDataCalcolo()); // Esegue questa istruzione come parte della logica del metodo.
            return repository.save(provvigione); // Restituisce il risultato dell'elaborazione al chiamante.
        }).orElseThrow(() -> new RuntimeException("Provvigione non trovata con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void deleteProvvigione(Long id) { // Elimina le provvigione identificato dall'input.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
