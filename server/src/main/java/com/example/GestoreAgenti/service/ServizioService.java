package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Servizio; // Importa com.example.GestoreAgenti.model.Servizio per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.ServizioRepository; // Importa com.example.GestoreAgenti.repository.ServizioRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ServizioService extends AbstractCrudService<Servizio, Long> { // Definisce la classe ServizioService che incapsula la logica applicativa.

    public ServizioService(ServizioRepository repository) { // Costruttore della classe ServizioService che inizializza le dipendenze necessarie.
        super(repository, new BeanCopyCrudEntityHandler<>("idServizio"), "Servizio"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.

    public List<Servizio> getAllServizi() { // Definisce il metodo getAllServizi che supporta la logica di dominio.
        return findAll(); // Restituisce il risultato dell'espressione findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<Servizio> getServizioById(Long id) { // Definisce il metodo getServizioById che supporta la logica di dominio.
        return findOptionalById(id); // Restituisce il risultato dell'espressione findOptionalById(id).
    } // Chiude il blocco di codice precedente.

    public Servizio createServizio(Servizio servizio) { // Definisce il metodo createServizio che supporta la logica di dominio.
        return create(servizio); // Restituisce il risultato dell'espressione create(servizio).
    } // Chiude il blocco di codice precedente.

    public Servizio updateServizio(Long id, Servizio servizioDetails) { // Definisce il metodo updateServizio che supporta la logica di dominio.
        return update(id, servizioDetails); // Restituisce il risultato dell'espressione update(id, servizioDetails).
    } // Chiude il blocco di codice precedente.

    public void deleteServizio(Long id) { // Definisce il metodo deleteServizio che supporta la logica di dominio.
        delete(id); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

