package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Servizio;
import com.example.GestoreAgenti.repository.ServizioRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class ServizioService extends AbstractCrudService<Servizio, Long> {

    public ServizioService(ServizioRepository repository) {
        super(repository, new BeanCopyCrudEntityHandler<>("idServizio"), "Servizio");
    }

    public List<Servizio> getAllServizi() {
        return findAll();
    }

    public Optional<Servizio> getServizioById(Long id) {
        return findOptionalById(id);
    }

    public Servizio createServizio(Servizio servizio) {
        return create(servizio);
    }

    public Servizio updateServizio(Long id, Servizio servizioDetails) {
        return update(id, servizioDetails);
    }

    public void deleteServizio(Long id) {
        delete(id);
    }
}

