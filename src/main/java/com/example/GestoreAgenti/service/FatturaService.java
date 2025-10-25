package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Fattura; // Importa la classe Fattura per accedere alle informazioni di fatturazione.
import com.example.GestoreAgenti.repository.FatturaRepository; // Importa FatturaRepository per delegare il salvataggio e la lettura delle fatture.

@Service // Applica l'annotazione @Service per configurare il componente.
public class FatturaService { // Dichiara la classe FatturaService che incapsula la logica del dominio.

    private final FatturaRepository repository; // Mantiene il riferimento al repository FatturaRepository per accedere ai dati persistenti.

    public FatturaService(FatturaRepository repository) { // Costruttore della classe FatturaService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Fattura> getAllFatture() { // Restituisce la lista di le fatture gestiti dal sistema.
        return repository.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Optional<Fattura> getFatturaById(Long id) { // Restituisce i dati di fattura filtrati in base a ID.
        return repository.findById(id); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Fattura createFattura(Fattura fattura) { // Metodo create fattura che gestisce la logica prevista.
        return repository.save(fattura); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Fattura updateFattura(Long id, Fattura fatturaDetails) { // Aggiorna la fattura applicando i dati forniti.
        return repository.findById(id).map(fattura -> { // Restituisce il risultato dell'elaborazione al chiamante.
            fattura.setNumeroFattura(fatturaDetails.getNumeroFattura()); // Esegue questa istruzione come parte della logica del metodo.
            fattura.setDataEmissione(fatturaDetails.getDataEmissione()); // Esegue questa istruzione come parte della logica del metodo.
            fattura.setCliente(fatturaDetails.getCliente()); // Esegue questa istruzione come parte della logica del metodo.
            fattura.setContratto(fatturaDetails.getContratto()); // Esegue questa istruzione come parte della logica del metodo.
            fattura.setImponibile(fatturaDetails.getImponibile()); // Esegue questa istruzione come parte della logica del metodo.
            fattura.setIva(fatturaDetails.getIva()); // Esegue questa istruzione come parte della logica del metodo.
            fattura.setTotale(fatturaDetails.getTotale()); // Esegue questa istruzione come parte della logica del metodo.
            fattura.setStato(fatturaDetails.getStato()); // Esegue questa istruzione come parte della logica del metodo.
            return repository.save(fattura); // Restituisce il risultato dell'elaborazione al chiamante.
        }).orElseThrow(() -> new RuntimeException("Fattura non trovata con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void deleteFattura(Long id) { // Elimina la fattura identificato dall'input.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

