package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.
import org.springframework.stereotype.Repository; // Importa Repository per identificare il bean come componente di accesso ai dati.

import com.example.GestoreAgenti.model.Fattura; // Importa la classe Fattura per accedere alle informazioni di fatturazione.

@Repository // Applica l'annotazione @Repository per configurare il componente.
public interface FatturaRepository extends JpaRepository<Fattura, Long> { // Dichiara l'interfaccia FatturaRepository che definisce le operazioni sui dati.
} // Chiude il blocco di codice precedente.

