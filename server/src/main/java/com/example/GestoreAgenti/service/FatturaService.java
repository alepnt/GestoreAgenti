package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.math.BigDecimal; // Importa java.math.BigDecimal per abilitare le funzionalità utilizzate nel file.
import java.time.LocalDate; // Importa java.time.LocalDate per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEventPublisher; // Importa com.example.GestoreAgenti.event.DomainEventPublisher per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.fattura.FatturaCreatedEvent; // Importa com.example.GestoreAgenti.event.fattura.FatturaCreatedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Fattura; // Importa com.example.GestoreAgenti.model.Fattura per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.state.AnnullataState; // Importa com.example.GestoreAgenti.model.state.AnnullataState per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.state.BozzaState; // Importa com.example.GestoreAgenti.model.state.BozzaState per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.state.EmessaState; // Importa com.example.GestoreAgenti.model.state.EmessaState per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.state.PagataState; // Importa com.example.GestoreAgenti.model.state.PagataState per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.FatturaRepository; // Importa com.example.GestoreAgenti.repository.FatturaRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class FatturaService extends AbstractCrudService<Fattura, Long> { // Definisce la classe FatturaService che incapsula la logica applicativa.

    private static final class FatturaCrudHandler extends BeanCopyCrudEntityHandler<Fattura> { // Apre il blocco di codice associato alla dichiarazione.

        private FatturaCrudHandler() { // Definisce il metodo FatturaCrudHandler che supporta la logica di dominio.
            super("idFattura", "state"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
        } // Chiude il blocco di codice precedente.

        @Override // Applica l'annotazione @Override per configurare il componente.
        public Fattura prepareForCreate(Fattura entity) { // Definisce il metodo prepareForCreate che supporta la logica di dominio.
            aggiornaStato(entity, entity.getStato()); // Esegue l'istruzione terminata dal punto e virgola.
            return entity; // Restituisce il risultato dell'espressione entity.
        } // Chiude il blocco di codice precedente.

        @Override // Applica l'annotazione @Override per configurare il componente.
        public Fattura merge(Fattura existing, Fattura changes) { // Definisce il metodo merge che supporta la logica di dominio.
            super.merge(existing, changes); // Esegue l'istruzione terminata dal punto e virgola.
            aggiornaStato(existing, changes.getStato()); // Esegue l'istruzione terminata dal punto e virgola.
            return existing; // Restituisce il risultato dell'espressione existing.
        } // Chiude il blocco di codice precedente.

        private void aggiornaStato(Fattura fattura, String statoRichiesto) { // Definisce il metodo aggiornaStato che supporta la logica di dominio.
            if (statoRichiesto == null || statoRichiesto.equalsIgnoreCase(fattura.getStato())) { // Valuta la condizione per controllare il flusso applicativo.
                return; // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.

            String statoNormalizzato = statoRichiesto.toUpperCase(); // Assegna il valore calcolato alla variabile String statoNormalizzato.
            if (PagataState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
                if (fattura.getStato().equalsIgnoreCase(BozzaState.NOME)) { // Valuta la condizione per controllare il flusso applicativo.
                    fattura.emetti(); // Esegue l'istruzione terminata dal punto e virgola.
                } // Chiude il blocco di codice precedente.
                fattura.paga(); // Esegue l'istruzione terminata dal punto e virgola.
                return; // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.

            if (EmessaState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
                fattura.emetti(); // Esegue l'istruzione terminata dal punto e virgola.
                return; // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.

            if (AnnullataState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
                fattura.annulla(); // Esegue l'istruzione terminata dal punto e virgola.
                return; // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.

            if (BozzaState.NOME.equals(statoNormalizzato)) { // Valuta la condizione per controllare il flusso applicativo.
                if (!fattura.getStato().equalsIgnoreCase(BozzaState.NOME)) { // Valuta la condizione per controllare il flusso applicativo.
                    throw new IllegalStateException("Impossibile tornare allo stato BOZZA da " + fattura.getStato()); // Propaga un'eccezione verso il chiamante.
                } // Chiude il blocco di codice precedente.
                return; // Esegue l'istruzione terminata dal punto e virgola.
            } // Chiude il blocco di codice precedente.

            throw new IllegalArgumentException("Stato fattura sconosciuto: " + statoRichiesto); // Propaga un'eccezione verso il chiamante.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    private final DomainEventPublisher eventPublisher; // Dichiara il campo eventPublisher dell'oggetto.

    public FatturaService(FatturaRepository repository, DomainEventPublisher eventPublisher) { // Costruttore della classe FatturaService che inizializza le dipendenze necessarie.
        super(repository, new FatturaCrudHandler(), "Fattura"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
        this.eventPublisher = eventPublisher; // Aggiorna il campo eventPublisher dell'istanza.
    } // Chiude il blocco di codice precedente.

    public List<Fattura> getAllFatture() { // Definisce il metodo getAllFatture che supporta la logica di dominio.
        return findAll(); // Restituisce il risultato dell'espressione findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<Fattura> getFatturaById(Long id) { // Definisce il metodo getFatturaById che supporta la logica di dominio.
        return findOptionalById(id); // Restituisce il risultato dell'espressione findOptionalById(id).
    } // Chiude il blocco di codice precedente.

    public Fattura createFattura(Fattura fattura) { // Definisce il metodo createFattura che supporta la logica di dominio.
        Fattura created = create(fattura); // Assegna il valore calcolato alla variabile Fattura created.
        eventPublisher.publish(new FatturaCreatedEvent(created)); // Esegue l'istruzione terminata dal punto e virgola.
        return created; // Restituisce il risultato dell'espressione created.
    } // Chiude il blocco di codice precedente.

    public Fattura updateFattura(Long id, Fattura fatturaDetails) { // Definisce il metodo updateFattura che supporta la logica di dominio.
        return update(id, fatturaDetails); // Restituisce il risultato dell'espressione update(id, fatturaDetails).
    } // Chiude il blocco di codice precedente.

    public void deleteFattura(Long id) { // Definisce il metodo deleteFattura che supporta la logica di dominio.
        delete(id); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    public Fattura emettiFattura(Long id) { // Definisce il metodo emettiFattura che supporta la logica di dominio.
        Fattura fattura = findRequiredById(id); // Assegna il valore calcolato alla variabile Fattura fattura.
        fattura.emetti(); // Esegue l'istruzione terminata dal punto e virgola.
        return repository().save(fattura); // Restituisce il risultato dell'espressione repository().save(fattura).
    } // Chiude il blocco di codice precedente.

    public Fattura pagaFattura(Long id) { // Definisce il metodo pagaFattura che supporta la logica di dominio.
        Fattura fattura = findRequiredById(id); // Assegna il valore calcolato alla variabile Fattura fattura.
        fattura.paga(); // Esegue l'istruzione terminata dal punto e virgola.
        return repository().save(fattura); // Restituisce il risultato dell'espressione repository().save(fattura).
    } // Chiude il blocco di codice precedente.

    public Fattura annullaFattura(Long id) { // Definisce il metodo annullaFattura che supporta la logica di dominio.
        Fattura fattura = findRequiredById(id); // Assegna il valore calcolato alla variabile Fattura fattura.
        fattura.annulla(); // Esegue l'istruzione terminata dal punto e virgola.
        return repository().save(fattura); // Restituisce il risultato dell'espressione repository().save(fattura).
    } // Chiude il blocco di codice precedente.

    public Fattura creaDaContratto(Contratto contratto, String numeroFattura, LocalDate dataEmissione, BigDecimal aliquotaIva) { // Definisce il metodo creaDaContratto che supporta la logica di dominio.
        Fattura fattura = GeneratoreFattura.getInstance() // Esegue l'istruzione necessaria alla logica applicativa.
                .creaDaContratto(contratto, numeroFattura, dataEmissione, aliquotaIva); // Esegue l'istruzione terminata dal punto e virgola.
        return createFattura(fattura); // Restituisce il risultato dell'espressione createFattura(fattura).
    } // Chiude il blocco di codice precedente.

    public Fattura creaDaContratto(Contratto contratto, String numeroFattura, BigDecimal aliquotaIva) { // Definisce il metodo creaDaContratto che supporta la logica di dominio.
        return creaDaContratto(contratto, numeroFattura, LocalDate.now(), aliquotaIva); // Restituisce il risultato dell'espressione creaDaContratto(contratto, numeroFattura, LocalDate.now(), aliquotaIva).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

