package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.
import org.springframework.stereotype.Repository; // Importa Repository per identificare il bean come componente di accesso ai dati.

import com.example.GestoreAgenti.model.Pagamento; // Importa la classe Pagamento per lavorare con i pagamenti gestiti dal servizio.

@Repository // Applica l'annotazione @Repository per configurare il componente.
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> { // Dichiara l'interfaccia PagamentoRepository che definisce le operazioni sui dati.
} // Chiude il blocco di codice precedente.

