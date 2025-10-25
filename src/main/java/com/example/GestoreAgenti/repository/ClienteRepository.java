package com.example.GestoreAgenti.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.GestoreAgenti.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}