package com.example.GestoreAgenti.repository; // Definisce il pacchetto com.example.GestoreAgenti.repository a cui appartiene questa classe.

import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository per ottenere le operazioni CRUD standard sul database.
import org.springframework.stereotype.Repository; // Importa Repository per identificare il bean come componente di accesso ai dati.

import com.example.GestoreAgenti.model.Fattura; // Importa la classe Fattura per accedere alle informazioni di fatturazione.

@Repository // Applica l'annotazione @Repository per configurare il componente.
public interface FatturaRepository extends JpaRepository<Fattura, Long> { // Dichiara l'interfaccia FatturaRepository che definisce le operazioni sui dati.

    java.util.List<Fattura> findByRegistrataTrueOrderByDataEmissioneDesc();

    java.util.List<Fattura> findByRegistrataFalseOrderByDataEmissioneDesc();

    @org.springframework.data.jpa.repository.Query("""
            select year(f.dataEmissione) as anno,
                   month(f.dataEmissione) as mese,
                   sum(f.totale) as totale
            from Fattura f
            where (:from is null or f.dataEmissione >= :from)
              and (:to is null or f.dataEmissione <= :to)
            group by year(f.dataEmissione), month(f.dataEmissione)
            order by year(f.dataEmissione), month(f.dataEmissione)
            """)
    java.util.List<com.example.GestoreAgenti.repository.projection.MonthlyRevenueProjection> sommaTotalePerMese(
            java.time.LocalDate from, java.time.LocalDate to);
} // Chiude il blocco di codice precedente.

