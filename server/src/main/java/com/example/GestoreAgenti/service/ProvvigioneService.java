package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Provvigione; // Importa com.example.GestoreAgenti.model.Provvigione per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.ProvvigioneRepository; // Importa com.example.GestoreAgenti.repository.ProvvigioneRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ProvvigioneService extends AbstractCrudService<Provvigione, Long> { // Definisce la classe ProvvigioneService che incapsula la logica applicativa.

    public ProvvigioneService(ProvvigioneRepository repository) { // Costruttore della classe ProvvigioneService che inizializza le dipendenze necessarie.
        super(repository, new BeanCopyCrudEntityHandler<>("idProvvigione"), "Provvigione"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.

    public List<Provvigione> getAllProvvigioni() { // Definisce il metodo getAllProvvigioni che supporta la logica di dominio.
        return findAll(); // Restituisce il risultato dell'espressione findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<Provvigione> getProvvigioneById(Long id) { // Definisce il metodo getProvvigioneById che supporta la logica di dominio.
        return findOptionalById(id); // Restituisce il risultato dell'espressione findOptionalById(id).
    } // Chiude il blocco di codice precedente.

    public Provvigione createProvvigione(Provvigione provvigione) { // Definisce il metodo createProvvigione che supporta la logica di dominio.
        return create(provvigione); // Restituisce il risultato dell'espressione create(provvigione).
    } // Chiude il blocco di codice precedente.

    public Provvigione updateProvvigione(Long id, Provvigione provvigioneDetails) { // Definisce il metodo updateProvvigione che supporta la logica di dominio.
        return update(id, provvigioneDetails); // Restituisce il risultato dell'espressione update(id, provvigioneDetails).
    } // Chiude il blocco di codice precedente.

    public void deleteProvvigione(Long id) { // Definisce il metodo deleteProvvigione che supporta la logica di dominio.
        delete(id); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    /**
     * Restituisce le provvigioni associate al dipendente indicato.
     *
     * @param dipendenteId identificativo del dipendente
     * @return elenco delle provvigioni disponibili per il dipendente
     */
    public java.util.List<Provvigione> getProvvigioniByDipendente(Long dipendenteId) { // Definisce il metodo getProvvigioniByDipendente che supporta la logica di dominio.
        return ((ProvvigioneRepository) repository()).findByDipendente_Id(dipendenteId); // Restituisce il risultato dell'espressione ((ProvvigioneRepository) repository()).findByDipendente_Id(dipendenteId).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
