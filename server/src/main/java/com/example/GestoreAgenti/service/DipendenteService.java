package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.event.DomainEventPublisher;
import com.example.GestoreAgenti.event.team.TeamMemberAddedEvent;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.repository.DipendenteRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class DipendenteService extends AbstractCrudService<Dipendente, Long> {

    private final DomainEventPublisher eventPublisher;

    public DipendenteService(DipendenteRepository repository, DomainEventPublisher eventPublisher) {
        super(repository, new BeanCopyCrudEntityHandler<>("id"), "Dipendente");
        this.eventPublisher = eventPublisher;
    }

    public List<Dipendente> findAll() {
        return super.findAll();
    }

    public Dipendente findById(Long id) {
        return findOptionalById(id).orElse(null);
    }

    public Dipendente save(Dipendente dipendente) {
        Dipendente created = create(dipendente);
        eventPublisher.publish(new TeamMemberAddedEvent(created));
        return created;
    }

    public Dipendente update(Long id, Dipendente dipendente) {
        return super.update(id, dipendente);
    }

    public Dipendente delete(Long id) {
        return super.delete(id);
    }
}
