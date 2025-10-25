package com.example.GestoreAgenti.service;


import com.example.GestoreAgenti.model.Team;
import com.example.GestoreAgenti.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository repository;

    public TeamService(TeamRepository repository) {
        this.repository = repository;
    }

    public List<Team> findAll() {
        return repository.findAll();
    }

    public Optional<Team> findById(Long id) {
        return repository.findById(id);
    }

    public Team save(Team team) {
        return repository.save(team);
    }

    public Team update(Long id, Team updatedTeam) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setProvincia(updatedTeam.getProvincia());
                    existing.setResponsabileId(updatedTeam.getResponsabileId());
                    existing.setTotProfittoMensile(updatedTeam.getTotProfittoMensile());
                    existing.setTotProvvigioneMensile(updatedTeam.getTotProvvigioneMensile());
                    existing.setTotProvvigioneAnnuo(updatedTeam.getTotProvvigioneAnnuo());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Team non trovato con id " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

