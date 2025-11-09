package com.example.GestoreAgenti.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestoreAgenti.model.agenda.AgendaEvento;

@Repository
public interface AgendaEventoRepository extends JpaRepository<AgendaEvento, Long> {

    List<AgendaEvento> findByDipendente_IdOrderByDataAscOraInizioAsc(Long dipendenteId);

    List<AgendaEvento> findByDipendente_IdAndDataBetweenOrderByDataAscOraInizioAsc(Long dipendenteId,
            LocalDate start, LocalDate end);
}
