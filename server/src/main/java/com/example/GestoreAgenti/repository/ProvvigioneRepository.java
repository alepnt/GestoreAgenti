package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.
import org.springframework.stereotype.Repository; // Importa Repository per identificare il bean come componente di accesso ai dati.

import com.example.GestoreAgenti.model.Provvigione; // Importa la classe Provvigione per trattare le provvigioni associate ai contratti.

@Repository // Applica l'annotazione @Repository per configurare il componente.
public interface ProvvigioneRepository extends JpaRepository<Provvigione, Long> { // Dichiara l'interfaccia ProvvigioneRepository che definisce le operazioni sui dati.

    /**
     * Recupera tutte le provvigioni associate al dipendente specificato.
     * L'uso del metodo derivato permette di mantenere il repository semplice e
     * di affidare al servizio applicativo eventuali regole aggiuntive.
     *
     * @param dipendenteId identificativo del dipendente di interesse
     * @return provvigioni collegate al dipendente
     */
    java.util.List<Provvigione> findByDipendente_Id(Long dipendenteId); // Esegue l'istruzione terminata dal punto e virgola.
} // Chiude il blocco di codice precedente.

