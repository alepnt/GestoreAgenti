package com.example.GestoreAgenti.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Team;
import com.example.GestoreAgenti.repository.TeamRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class TeamService extends AbstractCrudService<Team, Long> {

    private static final BigDecimal MIN_COMMISSION = new BigDecimal("0.10");
    private static final BigDecimal MAX_COMMISSION = new BigDecimal("0.12");

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository repository) {
        super(repository, new BeanCopyCrudEntityHandler<>("no"), "Team");
        this.teamRepository = repository;
    }

    public List<Team> findAll() {
        return super.findAll();
    }

    public Optional<Team> findById(Long id) {
        return findOptionalById(id);
    }

    public Team save(Team team) {
        validateTeam(team, null);
        return create(team);
    }

    public Team update(Long id, Team updatedTeam) {
        validateTeam(updatedTeam, id);
        return super.update(id, updatedTeam);
    }

    public Team delete(Long id) {
        Team existing = findRequiredById(id);
        if (existing.getResponsabileId() != null) {
            long count = teamRepository.countByResponsabileId(existing.getResponsabileId());
            if (count <= 2) {
                throw new IllegalStateException("Impossibile eliminare il team: il responsabile deve coordinare almeno due team");
            }
        }
        return super.delete(id);
    }

    private void validateTeam(Team team, Long id) {
        if (team.getPercentualeProvvigione() == null) {
            throw new IllegalArgumentException("La percentuale di provvigione del team è obbligatoria");
        }
        if (team.getPercentualeProvvigione().compareTo(MIN_COMMISSION) < 0
                || team.getPercentualeProvvigione().compareTo(MAX_COMMISSION) > 0) {
            throw new IllegalArgumentException("La percentuale di provvigione deve essere compresa tra 10% e 12%");
        }
        Long responsabileId = team.getResponsabileId();
        if (responsabileId != null) {
            long count = teamRepository.countByResponsabileId(responsabileId);
            if (id == null) {
                if (count >= 3) {
                    throw new IllegalStateException("Ogni responsabile può coordinare al massimo tre team");
                }
            } else {
                Team existing = findRequiredById(id);
                if (!Objects.equals(existing.getResponsabileId(), responsabileId) && count >= 3) {
                    throw new IllegalStateException("Ogni responsabile può coordinare al massimo tre team");
                }
            }
        }
        if (team.getDistribuzioneProvvigioni() == null) {
            throw new IllegalArgumentException("La modalità di distribuzione delle provvigioni è obbligatoria");
        }
        if (team.getBaseCalcolo() == null) {
            throw new IllegalArgumentException("È necessario specificare la base di calcolo delle provvigioni");
        }
    }
}
