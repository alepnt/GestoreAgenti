package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.repository.ContrattoRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class ContrattoService extends AbstractCrudService<Contratto, Long> {

    public ContrattoService(ContrattoRepository repository) {
        super(repository, new BeanCopyCrudEntityHandler<>("idContratto"), "Contratto");
    }

    public List<Contratto> getAllContratti() {
        return findAll();
    }

    public Optional<Contratto> getContrattoById(Long id) {
        return findOptionalById(id);
    }

    public Contratto createContratto(Contratto contratto) {
        return create(contratto);
    }

    public Contratto updateContratto(Long id, Contratto contrattoDetails) {
        return update(id, contrattoDetails);
    }

    public void deleteContratto(Long id) {
        delete(id);
    }
}
