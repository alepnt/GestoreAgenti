package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Provvigione;
import com.example.GestoreAgenti.repository.ProvvigioneRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class ProvvigioneService extends AbstractCrudService<Provvigione, Long> {

    public ProvvigioneService(ProvvigioneRepository repository) {
        super(repository, new BeanCopyCrudEntityHandler<>("idProvvigione"), "Provvigione");
    }

    public List<Provvigione> getAllProvvigioni() {
        return findAll();
    }

    public Optional<Provvigione> getProvvigioneById(Long id) {
        return findOptionalById(id);
    }

    public Provvigione createProvvigione(Provvigione provvigione) {
        return create(provvigione);
    }

    public Provvigione updateProvvigione(Long id, Provvigione provvigioneDetails) {
        return update(id, provvigioneDetails);
    }

    public void deleteProvvigione(Long id) {
        delete(id);
    }
}
