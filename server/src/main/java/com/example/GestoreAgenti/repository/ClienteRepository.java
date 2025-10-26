package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.

import com.example.GestoreAgenti.model.Cliente; // Importa la classe Cliente per gestire l'entità cliente nelle operazioni del controller.

public interface ClienteRepository extends JpaRepository<Cliente, Long> {} // Dichiara l'interfaccia ClienteRepository che definisce le operazioni sui dati.
