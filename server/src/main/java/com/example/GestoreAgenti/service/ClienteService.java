package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.repository.ClienteRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class ClienteService extends AbstractCrudService<Cliente, Long> {

    public ClienteService(ClienteRepository repository) {
        super(repository, new BeanCopyCrudEntityHandler<>("id"), "Cliente");
    }

    @Override
    public List<Cliente> findAll() {
        return super.findAll();
    }

    public Cliente findById(Long id) {
        return findOptionalById(id).orElse(null);
    }

    public Cliente save(Cliente cliente) {
        return create(cliente);
    }

    public Cliente update(Long id, Cliente cliente) {
        return super.update(id, cliente);
    }

    public Cliente delete(Long id) {
        return super.delete(id);
    }
}
