package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEventPublisher; // Importa com.example.GestoreAgenti.event.DomainEventPublisher per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.team.TeamMemberAddedEvent; // Importa com.example.GestoreAgenti.event.team.TeamMemberAddedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.DipendenteRepository; // Importa com.example.GestoreAgenti.repository.DipendenteRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class DipendenteService extends AbstractCrudService<Dipendente, Long> { // Definisce la classe DipendenteService che incapsula la logica applicativa.

    private final DomainEventPublisher eventPublisher; // Dichiara il campo eventPublisher dell'oggetto.

    public DipendenteService(DipendenteRepository repository, DomainEventPublisher eventPublisher) { // Costruttore della classe DipendenteService che inizializza le dipendenze necessarie.
        super(repository, new BeanCopyCrudEntityHandler<>("id"), "Dipendente"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
        this.eventPublisher = eventPublisher; // Aggiorna il campo eventPublisher dell'istanza.
    } // Chiude il blocco di codice precedente.

    public List<Dipendente> findAll() { // Definisce il metodo findAll che supporta la logica di dominio.
        return super.findAll(); // Restituisce il risultato dell'espressione super.findAll().
    } // Chiude il blocco di codice precedente.

    public Dipendente findById(Long id) { // Definisce il metodo findById che supporta la logica di dominio.
        return findOptionalById(id).orElse(null); // Restituisce il risultato dell'espressione findOptionalById(id).orElse(null).
    } // Chiude il blocco di codice precedente.

    public Dipendente save(Dipendente dipendente) { // Definisce il metodo save che supporta la logica di dominio.
        Dipendente created = create(dipendente); // Assegna il valore calcolato alla variabile Dipendente created.
        eventPublisher.publish(new TeamMemberAddedEvent(created)); // Esegue l'istruzione terminata dal punto e virgola.
        return created; // Restituisce il risultato dell'espressione created.
    } // Chiude il blocco di codice precedente.

    public Dipendente update(Long id, Dipendente dipendente) { // Definisce il metodo update che supporta la logica di dominio.
        return super.update(id, dipendente); // Restituisce il risultato dell'espressione super.update(id, dipendente).
    } // Chiude il blocco di codice precedente.

    public Dipendente delete(Long id) { // Definisce il metodo delete che supporta la logica di dominio.
        return super.delete(id); // Restituisce il risultato dell'espressione super.delete(id).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
