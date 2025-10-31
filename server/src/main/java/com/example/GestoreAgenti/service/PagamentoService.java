package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.event.DomainEventPublisher;
import com.example.GestoreAgenti.event.pagamento.PagamentoCreatedEvent;
import com.example.GestoreAgenti.event.pagamento.PagamentoDeletedEvent;
import com.example.GestoreAgenti.event.pagamento.PagamentoStatusChangedEvent;
import com.example.GestoreAgenti.event.pagamento.PagamentoUpdatedEvent;
import com.example.GestoreAgenti.model.Pagamento;
import com.example.GestoreAgenti.repository.PagamentoRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class PagamentoService extends AbstractCrudService<Pagamento, Long> {

    private final DomainEventPublisher eventPublisher;

    public PagamentoService(PagamentoRepository repository, DomainEventPublisher eventPublisher) {
        super(repository, new BeanCopyCrudEntityHandler<>("idPagamento", "state"), "Pagamento");
        this.eventPublisher = eventPublisher;
    }

    public List<Pagamento> getAllPagamenti() {
        return findAll();
    }

    public Optional<Pagamento> getPagamentoById(Long id) {
        return findOptionalById(id);
    }

    public Pagamento createPagamento(Pagamento pagamento) {
        Pagamento created = create(pagamento);
        eventPublisher.publish(new PagamentoCreatedEvent(created));
        return created;
    }

    public Pagamento updatePagamento(Long id, Pagamento pagamentoDetails) {
        Pagamento updated = update(id, pagamentoDetails);
        eventPublisher.publish(new PagamentoUpdatedEvent(updated));
        return updated;
    }

    public void deletePagamento(Long id) {
        Pagamento deleted = delete(id);
        eventPublisher.publish(new PagamentoDeletedEvent(deleted));
    }

    public Pagamento avviaElaborazione(Long id) {
        return aggiornaStato(id, Pagamento::elabora);
    }

    public Pagamento completaPagamento(Long id) {
        return aggiornaStato(id, Pagamento::completa);
    }

    public Pagamento fallisciPagamento(Long id) {
        return aggiornaStato(id, Pagamento::fallisci);
    }

    public Pagamento ripetiElaborazione(Long id) {
        return aggiornaStato(id, Pagamento::ripeti);
    }

    private Pagamento aggiornaStato(Long id, Consumer<Pagamento> operazione) {
        Pagamento pagamento = findRequiredById(id);
        String statoPrecedente = pagamento.getStato();
        operazione.accept(pagamento);
        Pagamento salvato = repository().save(pagamento);
        eventPublisher.publish(new PagamentoStatusChangedEvent(salvato, statoPrecedente, salvato.getStato()));
        return salvato;
    }
}

