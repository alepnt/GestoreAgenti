package com.example.GestoreAgenti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestoreAgenti.model.Servizio;

@Repository
public interface ServizioRepository extends JpaRepository<Servizio, Long> {
}
