package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Team;
import com.example.GestoreAgenti.repository.TeamRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class TeamService extends AbstractCrudService<Team, Long> {

    public TeamService(TeamRepository repository) {
        super(repository, new BeanCopyCrudEntityHandler<>("no"), "Team");
    }

    public List<Team> findAll() {
        return super.findAll();
    }

    public Optional<Team> findById(Long id) {
        return findOptionalById(id);
    }

    public Team save(Team team) {
        return create(team);
    }

    public Team update(Long id, Team updatedTeam) {
        return super.update(id, updatedTeam);
    }

    public Team delete(Long id) {
        return super.delete(id);
    }
}

