package com.example.GestoreAgenti.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestoreAgenti.model.Provvigione;

@Repository
public interface ProvvigioneRepository extends JpaRepository<Provvigione, Long> {

    List<Provvigione> findByDipendente_Id(Long dipendenteId);

    List<Provvigione> findByFattura_IdFattura(Long fatturaId);

    Optional<Provvigione> findByFattura_IdFatturaAndDipendente_Id(Long fatturaId, Long dipendenteId);
}
