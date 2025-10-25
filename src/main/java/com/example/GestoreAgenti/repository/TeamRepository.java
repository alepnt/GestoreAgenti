package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.


import com.example.GestoreAgenti.model.Team; // Importa la classe Team per manipolare i team gestiti dall'applicazione.
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.
import org.springframework.stereotype.Repository; // Importa Repository per identificare il bean come componente di accesso ai dati.

@Repository // Applica l'annotazione @Repository per configurare il componente.
public interface TeamRepository extends JpaRepository<Team, Long> { // Dichiara l'interfaccia TeamRepository che definisce le operazioni sui dati.
} // Chiude il blocco di codice precedente.
