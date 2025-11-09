package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.
import java.util.function.Consumer; // Importa java.util.function.Consumer per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.event.DomainEventPublisher; // Importa com.example.GestoreAgenti.event.DomainEventPublisher per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.pagamento.PagamentoCreatedEvent; // Importa com.example.GestoreAgenti.event.pagamento.PagamentoCreatedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.pagamento.PagamentoDeletedEvent; // Importa com.example.GestoreAgenti.event.pagamento.PagamentoDeletedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.pagamento.PagamentoStatusChangedEvent; // Importa com.example.GestoreAgenti.event.pagamento.PagamentoStatusChangedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.event.pagamento.PagamentoUpdatedEvent; // Importa com.example.GestoreAgenti.event.pagamento.PagamentoUpdatedEvent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Pagamento; // Importa com.example.GestoreAgenti.model.Pagamento per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.PagamentoRepository; // Importa com.example.GestoreAgenti.repository.PagamentoRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class PagamentoService extends AbstractCrudService<Pagamento, Long> { // Definisce la classe PagamentoService che incapsula la logica applicativa.

    private final DomainEventPublisher eventPublisher; // Dichiara il campo eventPublisher dell'oggetto.

    public PagamentoService(PagamentoRepository repository, DomainEventPublisher eventPublisher) { // Costruttore della classe PagamentoService che inizializza le dipendenze necessarie.
        super(repository, new BeanCopyCrudEntityHandler<>("idPagamento", "state"), "Pagamento"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
        this.eventPublisher = eventPublisher; // Aggiorna il campo eventPublisher dell'istanza.
    } // Chiude il blocco di codice precedente.

    public List<Pagamento> getAllPagamenti() { // Definisce il metodo getAllPagamenti che supporta la logica di dominio.
        return findAll(); // Restituisce il risultato dell'espressione findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<Pagamento> getPagamentoById(Long id) { // Definisce il metodo getPagamentoById che supporta la logica di dominio.
        return findOptionalById(id); // Restituisce il risultato dell'espressione findOptionalById(id).
    } // Chiude il blocco di codice precedente.

    public Pagamento createPagamento(Pagamento pagamento) { // Definisce il metodo createPagamento che supporta la logica di dominio.
        Pagamento created = create(pagamento); // Assegna il valore calcolato alla variabile Pagamento created.
        eventPublisher.publish(new PagamentoCreatedEvent(created)); // Esegue l'istruzione terminata dal punto e virgola.
        return created; // Restituisce il risultato dell'espressione created.
    } // Chiude il blocco di codice precedente.

    public Pagamento updatePagamento(Long id, Pagamento pagamentoDetails) { // Definisce il metodo updatePagamento che supporta la logica di dominio.
        Pagamento updated = update(id, pagamentoDetails); // Assegna il valore calcolato alla variabile Pagamento updated.
        eventPublisher.publish(new PagamentoUpdatedEvent(updated)); // Esegue l'istruzione terminata dal punto e virgola.
        return updated; // Restituisce il risultato dell'espressione updated.
    } // Chiude il blocco di codice precedente.

    public void deletePagamento(Long id) { // Definisce il metodo deletePagamento che supporta la logica di dominio.
        Pagamento deleted = delete(id); // Assegna il valore calcolato alla variabile Pagamento deleted.
        eventPublisher.publish(new PagamentoDeletedEvent(deleted)); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    public Pagamento avviaElaborazione(Long id) { // Definisce il metodo avviaElaborazione che supporta la logica di dominio.
        return aggiornaStato(id, Pagamento::elabora); // Restituisce il risultato dell'espressione aggiornaStato(id, Pagamento::elabora).
    } // Chiude il blocco di codice precedente.

    public Pagamento completaPagamento(Long id) { // Definisce il metodo completaPagamento che supporta la logica di dominio.
        return aggiornaStato(id, Pagamento::completa); // Restituisce il risultato dell'espressione aggiornaStato(id, Pagamento::completa).
    } // Chiude il blocco di codice precedente.

    public Pagamento fallisciPagamento(Long id) { // Definisce il metodo fallisciPagamento che supporta la logica di dominio.
        return aggiornaStato(id, Pagamento::fallisci); // Restituisce il risultato dell'espressione aggiornaStato(id, Pagamento::fallisci).
    } // Chiude il blocco di codice precedente.

    public Pagamento ripetiElaborazione(Long id) { // Definisce il metodo ripetiElaborazione che supporta la logica di dominio.
        return aggiornaStato(id, Pagamento::ripeti); // Restituisce il risultato dell'espressione aggiornaStato(id, Pagamento::ripeti).
    } // Chiude il blocco di codice precedente.

    private Pagamento aggiornaStato(Long id, Consumer<Pagamento> operazione) { // Definisce il metodo aggiornaStato che supporta la logica di dominio.
        Pagamento pagamento = findRequiredById(id); // Assegna il valore calcolato alla variabile Pagamento pagamento.
        String statoPrecedente = pagamento.getStato(); // Assegna il valore calcolato alla variabile String statoPrecedente.
        operazione.accept(pagamento); // Esegue l'istruzione terminata dal punto e virgola.
        Pagamento salvato = repository().save(pagamento); // Assegna il valore calcolato alla variabile Pagamento salvato.
        eventPublisher.publish(new PagamentoStatusChangedEvent(salvato, statoPrecedente, salvato.getStato())); // Esegue l'istruzione terminata dal punto e virgola.
        return salvato; // Restituisce il risultato dell'espressione salvato.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

