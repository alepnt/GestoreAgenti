package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.


import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.ContrattoRepository; // Importa com.example.GestoreAgenti.repository.ContrattoRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ContrattoService extends AbstractCrudService<Contratto, Long> { // Definisce la classe ContrattoService che incapsula la logica applicativa.

    public ContrattoService(ContrattoRepository repository) { // Costruttore della classe ContrattoService che inizializza le dipendenze necessarie.
        super(repository, new BeanCopyCrudEntityHandler<>("idContratto"), "Contratto"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.

    public List<Contratto> getAllContratti() { // Definisce il metodo getAllContratti che supporta la logica di dominio.
        return findAll(); // Restituisce il risultato dell'espressione findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<Contratto> getContrattoById(Long id) { // Definisce il metodo getContrattoById che supporta la logica di dominio.
        return findOptionalById(id); // Restituisce il risultato dell'espressione findOptionalById(id).
    } // Chiude il blocco di codice precedente.

    public Contratto createContratto(Contratto contratto) { // Definisce il metodo createContratto che supporta la logica di dominio.
        return create(contratto); // Restituisce il risultato dell'espressione create(contratto).
    } // Chiude il blocco di codice precedente.

    public Contratto updateContratto(Long id, Contratto contrattoDetails) { // Definisce il metodo updateContratto che supporta la logica di dominio.
        return update(id, contrattoDetails); // Restituisce il risultato dell'espressione update(id, contrattoDetails).
    } // Chiude il blocco di codice precedente.

    public void deleteContratto(Long id) { // Definisce il metodo deleteContratto che supporta la logica di dominio.
        delete(id); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    /**
     * Recupera tutti i contratti collegati al dipendente indicato.
     *
     * @param dipendenteId identificativo del dipendente
     * @return elenco dei contratti associati
     */
    public java.util.List<Contratto> getContrattiByDipendente(Long dipendenteId) { // Definisce il metodo getContrattiByDipendente che supporta la logica di dominio.
        return ordinaPerData(((ContrattoRepository) repository()).findByDipendente_Id(dipendenteId));
    } // Chiude il blocco di codice precedente.

    public java.util.List<Contratto> getContrattiByCliente(Long clienteId) {
        return ordinaPerData(((ContrattoRepository) repository()).findByCliente_Id(clienteId));
    }

    public java.util.List<Contratto> getContrattiByTeam(String team) {
        return ordinaPerData(((ContrattoRepository) repository()).findByDipendente_TeamIgnoreCase(team));
    }

    private java.util.List<Contratto> ordinaPerData(java.util.List<Contratto> contratti) {
        contratti.sort(java.util.Comparator.comparing(Contratto::getDataInizio,
                java.util.Comparator.nullsLast(java.time.LocalDate::compareTo)));
        return contratti;
    }
} // Chiude il blocco di codice precedente.
