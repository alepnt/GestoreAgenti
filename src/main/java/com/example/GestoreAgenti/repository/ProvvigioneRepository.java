package com.example.GestoreAgenti.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.GestoreAgenti.model.Provvigione;

@Repository
public interface ProvvigioneRepository extends JpaRepository<Provvigione, Long> {
}

