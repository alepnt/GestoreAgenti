package com.example.GestoreAgenti.model.commission;

/**
 * Modalità di distribuzione delle provvigioni all'interno di un team.
 */
public enum CommissionDistributionMode {
    /**
     * Ogni agente riceve la quota indicata nel proprio contratto.
     */
    PERCENTUALE,
    /**
     * Le quote vengono assegnate seguendo il ranking fino all'esaurimento
     * della percentuale totale prevista per il team.
     */
    SBARRAMENTO
}
