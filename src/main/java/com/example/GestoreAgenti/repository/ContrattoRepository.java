package com.example.GestoreAgenti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestoreAgenti.model.Contratto;

@Repository
public interface ContrattoRepository extends JpaRepository<Contratto, Long> {}

