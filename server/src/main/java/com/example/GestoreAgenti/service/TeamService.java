package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.


import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Service; // Importa org.springframework.stereotype.Service per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Team; // Importa com.example.GestoreAgenti.model.Team per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.repository.TeamRepository; // Importa com.example.GestoreAgenti.repository.TeamRepository per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.AbstractCrudService; // Importa com.example.GestoreAgenti.service.crud.AbstractCrudService per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler; // Importa com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler per abilitare le funzionalità utilizzate nel file.

@Service // Applica l'annotazione @Service per configurare il componente.
public class TeamService extends AbstractCrudService<Team, Long> { // Definisce la classe TeamService che incapsula la logica applicativa.

    public TeamService(TeamRepository repository) { // Costruttore della classe TeamService che inizializza le dipendenze necessarie.
        super(repository, new BeanCopyCrudEntityHandler<>("no"), "Team"); // Invoca il costruttore della superclasse per inizializzare lo stato ereditato.
    } // Chiude il blocco di codice precedente.

    public List<Team> findAll() { // Definisce il metodo findAll che supporta la logica di dominio.
        return super.findAll(); // Restituisce il risultato dell'espressione super.findAll().
    } // Chiude il blocco di codice precedente.

    public Optional<Team> findById(Long id) { // Definisce il metodo findById che supporta la logica di dominio.
        return findOptionalById(id); // Restituisce il risultato dell'espressione findOptionalById(id).
    } // Chiude il blocco di codice precedente.

    public Team save(Team team) { // Definisce il metodo save che supporta la logica di dominio.
        return create(team); // Restituisce il risultato dell'espressione create(team).
    } // Chiude il blocco di codice precedente.

    public Team update(Long id, Team updatedTeam) { // Definisce il metodo update che supporta la logica di dominio.
        return super.update(id, updatedTeam); // Restituisce il risultato dell'espressione super.update(id, updatedTeam).
    } // Chiude il blocco di codice precedente.

    public Team delete(Long id) { // Definisce il metodo delete che supporta la logica di dominio.
        return super.delete(id); // Restituisce il risultato dell'espressione super.delete(id).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.

