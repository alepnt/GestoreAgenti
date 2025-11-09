package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.ClienteRepository; // Importa com.example.GestoreAgenti.repository.ClienteRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class ClienteService extends AbstractCrudService<Cliente, Long> { // Definisce la classe ClienteService che incapsula la logica applicativa.

    public ClienteService(ClienteRepository repository) { // Costruttore della classe ClienteService che inizializza le dipendenze necessarie.
        super(repository, new BeanCopyCrudEntityHandler<>("id"), "Cliente"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public List<Cliente> findAll() { // Definisce il metodo findAll che supporta la logica di dominio.
        return super.findAll(); // Restituisce il risultato dell'espressione super.findAll().
    } // Chiude il blocco di codice precedente.

    public Cliente findById(Long id) { // Definisce il metodo findById che supporta la logica di dominio.
        return findOptionalById(id).orElse(null); // Restituisce il risultato dell'espressione findOptionalById(id).orElse(null).
    } // Chiude il blocco di codice precedente.

    public Cliente save(Cliente cliente) { // Definisce il metodo save che supporta la logica di dominio.
        return create(cliente); // Restituisce il risultato dell'espressione create(cliente).
    } // Chiude il blocco di codice precedente.

    public Cliente update(Long id, Cliente cliente) { // Definisce il metodo update che supporta la logica di dominio.
        return super.update(id, cliente); // Restituisce il risultato dell'espressione super.update(id, cliente).
    } // Chiude il blocco di codice precedente.

    public Cliente delete(Long id) { // Definisce il metodo delete che supporta la logica di dominio.
        return super.delete(id); // Restituisce il risultato dell'espressione super.delete(id).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
