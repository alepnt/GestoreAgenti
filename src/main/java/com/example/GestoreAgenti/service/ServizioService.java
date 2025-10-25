package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Servizio; // Importa la classe Servizio per utilizzare i dettagli dei servizi commercializzati.
import com.example.GestoreAgenti.repository.ServizioRepository; // Importa ServizioRepository per eseguire le operazioni sui servizi.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ServizioService { // Dichiara la classe ServizioService che incapsula la logica del dominio.

    private final ServizioRepository repository; // Mantiene il riferimento al repository ServizioRepository per accedere ai dati persistenti.

    public ServizioService(ServizioRepository repository) { // Costruttore della classe ServizioService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Servizio> getAllServizi() { // Restituisce la lista di i servizi gestiti dal sistema.
        return repository.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Optional<Servizio> getServizioById(Long id) { // Restituisce i dati di servizio filtrati in base a ID.
        return repository.findById(id); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Servizio createServizio(Servizio servizio) { // Metodo create servizio che gestisce la logica prevista.
        return repository.save(servizio); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Servizio updateServizio(Long id, Servizio servizioDetails) { // Aggiorna il servizio applicando i dati forniti.
        return repository.findById(id).map(servizio -> { // Restituisce il risultato dell'elaborazione al chiamante.
            servizio.setNome(servizioDetails.getNome()); // Esegue questa istruzione come parte della logica del metodo.
            servizio.setDescrizione(servizioDetails.getDescrizione()); // Esegue questa istruzione come parte della logica del metodo.
            servizio.setPrezzoBase(servizioDetails.getPrezzoBase()); // Esegue questa istruzione come parte della logica del metodo.
            servizio.setCommissionePercentuale(servizioDetails.getCommissionePercentuale()); // Esegue questa istruzione come parte della logica del metodo.
            return repository.save(servizio); // Restituisce il risultato dell'elaborazione al chiamante.
        }).orElseThrow(() -> new RuntimeException("Servizio non trovato con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void deleteServizio(Long id) { // Elimina il servizio identificato dall'input.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

