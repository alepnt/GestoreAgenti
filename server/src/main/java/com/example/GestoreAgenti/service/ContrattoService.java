package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.


import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Contratto; // Importa la classe Contratto per manipolare i contratti esposti dall'API.
import com.example.GestoreAgenti.repository.ContrattoRepository; // Importa ContrattoRepository per effettuare operazioni di persistenza sui contratti.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ContrattoService { // Dichiara la classe ContrattoService che incapsula la logica del dominio.

    private final ContrattoRepository repository; // Mantiene il riferimento al repository ContrattoRepository per accedere ai dati persistenti.

    public ContrattoService(ContrattoRepository repository) { // Costruttore della classe ContrattoService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Contratto> getAllContratti() { // Restituisce la lista di i contratti gestiti dal sistema.
        return repository.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Optional<Contratto> getContrattoById(Long id) { // Restituisce i dati di contratto filtrati in base a ID.
        return repository.findById(id); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Contratto createContratto(Contratto contratto) { // Metodo create contratto che gestisce la logica prevista.
        return repository.save(contratto); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Contratto updateContratto(Long id, Contratto contrattoDetails) { // Aggiorna il contratto applicando i dati forniti.
        return repository.findById(id).map(contratto -> { // Restituisce il risultato dell'elaborazione al chiamante.
            contratto.setCliente(contrattoDetails.getCliente()); // Esegue questa istruzione come parte della logica del metodo.
            contratto.setDipendente(contrattoDetails.getDipendente()); // Esegue questa istruzione come parte della logica del metodo.
            contratto.setServizio(contrattoDetails.getServizio()); // Esegue questa istruzione come parte della logica del metodo.
            contratto.setDataInizio(contrattoDetails.getDataInizio()); // Esegue questa istruzione come parte della logica del metodo.
            contratto.setDataFine(contrattoDetails.getDataFine()); // Esegue questa istruzione come parte della logica del metodo.
            contratto.setImporto(contrattoDetails.getImporto()); // Esegue questa istruzione come parte della logica del metodo.
            contratto.setStato(contrattoDetails.getStato()); // Esegue questa istruzione come parte della logica del metodo.
            contratto.setNote(contrattoDetails.getNote()); // Esegue questa istruzione come parte della logica del metodo.
            return repository.save(contratto); // Restituisce il risultato dell'elaborazione al chiamante.
        }).orElseThrow(() -> new RuntimeException("Contratto non trovato con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void deleteContratto(Long id) { // Elimina il contratto identificato dall'input.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
