package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.repository.DipendenteRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class DipendenteService extends AbstractCrudService<Dipendente, Long> {

    public DipendenteService(DipendenteRepository repository) {
        super(repository, new BeanCopyCrudEntityHandler<>("id"), "Dipendente");
    }

    public List<Dipendente> findAll() {
        return super.findAll();
    }

    public Dipendente findById(Long id) {
        return findOptionalById(id).orElse(null);
    }

    public Dipendente save(Dipendente dipendente) {
        return create(dipendente);
    }

    public Dipendente update(Long id, Dipendente dipendente) {
        return super.update(id, dipendente);
    }

    public void delete(Long id) {
        super.delete(id);
    }
}
