package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.

import com.example.GestoreAgenti.model.Dipendente; // Importa la classe Dipendente per operare sui dati dei dipendenti.

public interface DipendenteRepository extends JpaRepository<Dipendente, Long> { // Dichiara l'interfaccia DipendenteRepository che definisce le operazioni sui dati.

    List<Dipendente> findByTeamIgnoreCase(String team); // Esegue l'istruzione terminata dal punto e virgola.

    Optional<Dipendente> findByEmailIgnoreCase(String email); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.
