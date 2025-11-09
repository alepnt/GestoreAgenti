package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.
import org.springframework.stereotype.Repository; // Importa Repository per identificare il bean come componente di accesso ai dati.

import com.example.GestoreAgenti.model.Contratto; // Importa la classe Contratto per manipolare i contratti esposti dall'API.

@Repository // Applica l'annotazione @Repository per configurare il componente.
public interface ContrattoRepository extends JpaRepository<Contratto, Long> { // Dichiara l'interfaccia ContrattoRepository che definisce le operazioni sui dati.

    /**
     * Recupera i contratti collegati al dipendente identificato dall'argomento.
     * La query derivata consente di ottenere rapidamente i dati necessari alle
     * viste aggregate offerte dalla facciata del dominio agenti.
     *
     * @param dipendenteId identificativo del dipendente
     * @return contratti associati al dipendente
     */
    java.util.List<Contratto> findByDipendente_Id(Long dipendenteId); // Esegue l'istruzione terminata dal punto e virgola.

    java.util.List<Contratto> findByCliente_Id(Long clienteId);

    java.util.List<Contratto> findByDipendente_TeamIgnoreCase(String team);
} // Chiude il blocco di codice precedente.

