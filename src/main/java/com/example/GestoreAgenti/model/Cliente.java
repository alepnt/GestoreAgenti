package com.example.GestoreAgenti.model;

import jakarta.persistence.Entity;

@Entity
public class Cliente extends Persona {

    private String type;
    private String ragioneSociale;
    private String partitaIva;

    // Getters e Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRagioneSociale() { return ragioneSociale; }
    public void setRagioneSociale(String ragioneSociale) { this.ragioneSociale = ragioneSociale; }

    public String getPartitaIva() { return partitaIva; }
    public void setPartitaIva(String partitaIva) { this.partitaIva = partitaIva; }
}
