package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa List per gestire insiemi ordinati di elementi.
import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.stereotype.Service; // Importa Service per dichiarare la classe come servizio dell'applicazione.

import com.example.GestoreAgenti.model.Fattura; // Importa la classe Fattura per accedere alle informazioni di fatturazione.
import com.example.GestoreAgenti.model.state.AnnullataState; // Importa la costante dello stato annullato.
import com.example.GestoreAgenti.model.state.BozzaState; // Importa la costante dello stato bozza.
import com.example.GestoreAgenti.model.state.EmessaState; // Importa la costante dello stato emesso.
import com.example.GestoreAgenti.model.state.PagataState; // Importa la costante dello stato pagato.
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
            aggiornaStato(fattura, fatturaDetails.getStato()); // Aggiorna lo stato rispettando le regole di transizione.
            return repository.save(fattura); // Restituisce il risultato dell'elaborazione al chiamante.
        }).orElseThrow(() -> new RuntimeException("Fattura non trovata con id " + id)); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    } // Chiude il blocco di codice precedente.

    public void deleteFattura(Long id) { // Elimina la fattura identificato dall'input.
        repository.deleteById(id); // Esegue questa istruzione come parte della logica del metodo.
    } // Chiude il blocco di codice precedente.

    public Fattura emettiFattura(Long id) { // Richiede l'emissione di una fattura rispettando le regole di stato.
        Fattura fattura = getRequiredFattura(id); // Recupera la fattura oppure solleva un'eccezione se assente.
        fattura.emetti(); // Delega la logica di transizione allo stato corrente.
        return repository.save(fattura); // Persiste lo stato aggiornato.
    } // Chiude il blocco di codice precedente.

    public Fattura pagaFattura(Long id) { // Richiede il pagamento di una fattura rispettando le regole di stato.
        Fattura fattura = getRequiredFattura(id); // Recupera la fattura oppure solleva un'eccezione se assente.
        fattura.paga(); // Delega la logica di transizione allo stato corrente.
        return repository.save(fattura); // Persiste lo stato aggiornato.
    } // Chiude il blocco di codice precedente.

    public Fattura annullaFattura(Long id) { // Richiede l'annullamento di una fattura rispettando le regole di stato.
        Fattura fattura = getRequiredFattura(id); // Recupera la fattura oppure solleva un'eccezione se assente.
        fattura.annulla(); // Delega la logica di transizione allo stato corrente.
        return repository.save(fattura); // Persiste lo stato aggiornato.
    } // Chiude il blocco di codice precedente.

    private Fattura getRequiredFattura(Long id) {
        return repository.findById(id) // Recupera la fattura richiesta.
                .orElseThrow(() -> new RuntimeException("Fattura non trovata con id " + id)); // Solleva eccezione in assenza del record.
    } // Chiude il blocco di codice precedente.

    private void aggiornaStato(Fattura fattura, String statoRichiesto) { // Aggiorna lo stato applicando le regole del pattern State.
        if (statoRichiesto == null || statoRichiesto.equalsIgnoreCase(fattura.getStato())) {
            return; // Nessun aggiornamento necessario.
        }

        String statoNormalizzato = statoRichiesto.toUpperCase();
        if (PagataState.NOME.equals(statoNormalizzato)) {
            if (fattura.getStato().equalsIgnoreCase(BozzaState.NOME)) {
                fattura.emetti(); // Porta la fattura allo stato EMESSA prima del pagamento.
            }
            fattura.paga();
            return;
        }

        if (EmessaState.NOME.equals(statoNormalizzato)) {
            fattura.emetti();
            return;
        }

        if (AnnullataState.NOME.equals(statoNormalizzato)) {
            fattura.annulla();
            return;
        }

        if (BozzaState.NOME.equals(statoNormalizzato)) {
            if (!fattura.getStato().equalsIgnoreCase(BozzaState.NOME)) {
                throw new IllegalStateException("Impossibile tornare allo stato BOZZA da " + fattura.getStato());
            }
            return; // Già in stato BOZZA.
        }

        throw new IllegalArgumentException("Stato fattura sconosciuto: " + statoRichiesto);
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

