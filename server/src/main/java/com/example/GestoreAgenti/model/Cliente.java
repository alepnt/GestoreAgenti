package com.example.GestoreAgenti.model; // Definisce il pacchetto com.example.GestoreAgenti.model a cui appartiene questa classe.

import jakarta.persistence.Entity; // Importa Entity per contrassegnare la classe come entità JPA.

@Entity // Applica l'annotazione @Entity per configurare il componente.
public class Cliente extends Persona { // Dichiara la classe Cliente che incapsula la logica del dominio.

    private String type; // Memorizza il tipo dell'entità.
    private String ragioneSociale; // Memorizza la ragione sociale dell'entità.
    private String partitaIva; // Memorizza la partita IVA dell'entità.

    // Getters e Setters
    public String getType() { return type; } // Restituisce il tipo dell'entità.
    public void setType(String type) { this.type = type; } // Imposta il tipo per l'entità.

    public String getRagioneSociale() { return ragioneSociale; } // Restituisce la ragione sociale dell'entità.
    public void setRagioneSociale(String ragioneSociale) { this.ragioneSociale = ragioneSociale; } // Imposta la ragione sociale per l'entità.

    public String getPartitaIva() { return partitaIva; } // Restituisce la partita IVA dell'entità.
    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; } // Imposta la partita IVA per l'entità.
} // Chiude il blocco di codice precedente.
