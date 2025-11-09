package com.example.GestoreAgenti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestoreAgenti.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByProvinciaIgnoreCase(String provincia);

    long countByResponsabileId(Long responsabileId);
}
