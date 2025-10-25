package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Pagamento; // Importa la classe Pagamento per lavorare con i pagamenti gestiti dal servizio.
import com.example.GestoreAgenti.repository.PagamentoRepository; // Importa PagamentoRepository per effettuare query sui pagamenti.

@Service // Applica l'annotazione @Service per configurare il componente.
public class PagamentoService { // Dichiara la classe PagamentoService che incapsula la logica del dominio.

    private final PagamentoRepository repository; // Mantiene il riferimento al repository PagamentoRepository per accedere ai dati persistenti.

    public PagamentoService(PagamentoRepository repository) { // Costruttore della classe PagamentoService che inizializza le dipendenze richieste.
        this.repository = repository; // Aggiorna il campo dell'istanza con il valore ricevuto.
    } // Chiude il blocco di codice precedente.

    public List<Pagamento> getAllPagamenti() { // Restituisce la lista di i pagamenti gestiti dal sistema.
        return repository.findAll(); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Optional<Pagamento> getPagamentoById(Long id) { // Restituisce i dati di pagamento filtrati in base a ID.
        return repository.findById(id); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Pagamento createPagamento(Pagamento pagamento) { // Metodo create pagamento che gestisce la logica prevista.
        return repository.save(pagamento); // Restituisce il risultato dell'elaborazione al chiamante.
    } // Chiude il blocco di codice precedente.

    public Pagamento updatePagamento(Long id, Pagamento pagamentoDetails) { // Aggiorna il pagamento applicando i dati forniti.
        return repository.findById(id).map(pagamento -> { // Restituisce il risultato dell'elaborazione al chiamante.
            pagamento.setFattura(pagamentoDetails.getFattura()); // Esegue questa istruzione come parte della logica del metodo.
            pagamento.setDataPagamento(pagamentoDetails.getDataPagamento()); // Esegue questa istruzione come parte della logica del metodo.
            pagamento.setImporto(pagamentoDetails.getImporto()); // Esegue questa istruzione come parte della logica del metodo.
            pagamento.setMetodo(pagamentoDetails.getMetodo()); // Esegue questa istruzione come parte della logica del metodo.
            return repository.save(pagamento); // Restituisce il risultato dell'elaborazione al chiamante.
        }).orElseThrow(() -> new RuntimeException("Pagamento non trovato con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void deletePagamento(Long id) { // Elimina il pagamento identificato dall'input.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

