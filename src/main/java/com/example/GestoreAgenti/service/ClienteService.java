package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Cliente; // Importa la classe Cliente per gestire l'entità cliente nelle operazioni del controller.
import com.example.GestoreAgenti.repository.ClienteRepository; // Importa ClienteRepository per delegare l'accesso persistente ai clienti.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ClienteService { // Dichiara la classe ClienteService che incapsula la logica del dominio.

    private final ClienteRepository repository; // Mantiene il riferimento al repository ClienteRepository per accedere ai dati persistenti.

    public ClienteService(ClienteRepository repository) { // Costruttore della classe ClienteService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Cliente> findAll() { return repository.findAll(); } // Restituisce tutti gli elementi gestiti dal servizio.

    public Cliente findById(Long id) { return repository.findById(id).orElse(null); } // Ricerca l'entità in base a ID.

    public Cliente save(Cliente cliente) { return repository.save(cliente); } // Salva l'entità persistendola nel database.

    public Cliente update(Long id, Cliente nuovo) { // Aggiorna l'entità applicando i dati forniti.
        Cliente esistente = findById(id); // Esegue questa istruzione come parte della logica del metodo.
        if (esistente == null) return null; // Valuta la condizione per determinare il ramo di esecuzione.
        nuovo.setId(id); // Esegue questa istruzione come parte della logica del metodo.
        return repository.save(nuovo); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public void delete(Long id) { repository.deleteById(id); } // Elimina l'entità identificata dal parametro in ingresso.
} // Chiude il blocco di codice precedente.
