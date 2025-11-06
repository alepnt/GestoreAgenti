package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import java.util.Optional; // Importa Optional per modellare risultati potenzialmente assenti.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.
import org.springframework.stereotype.Repository; // Importa Repository per identificare il bean come componente di accesso ai dati.

import com.example.GestoreAgenti.model.Utente; // Importa la classe Utente per utilizzare i dati anagrafici degli utenti.

@Repository // Applica l'annotazione @Repository per configurare il componente.
public interface UtenteRepository extends JpaRepository<Utente, Long> { // Dichiara l'interfaccia UtenteRepository che definisce le operazioni sui dati.
    Optional<Utente> findByUsername(String username); // Elabora il risultato opzionale scegliendo il comportamento appropriato.
    Optional<Utente> findByDipendente_Id(Long dipendenteId); // Recupera l'utente collegato al dipendente richiesto.
    Optional<Utente> findByCliente_Id(Long clienteId); // Recupera l'utente collegato al cliente richiesto.
} // Chiude il blocco di codice precedente.

