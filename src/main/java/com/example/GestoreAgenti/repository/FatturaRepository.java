package com.example.GestoreAgenti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestoreAgenti.model.Fattura;

@Repository
public interface FatturaRepository extends JpaRepository<Fattura, Long> {
}

