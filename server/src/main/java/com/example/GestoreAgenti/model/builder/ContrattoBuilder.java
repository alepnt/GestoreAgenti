package com.example.GestoreAgenti.model.builder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Servizio;

/**
 * Builder per creare contratti garantendo la coerenza del periodo e degli importi.
 */
public final class ContrattoBuilder {

    private final Contratto contratto;

    private ContrattoBuilder(Cliente cliente, Dipendente dipendente, Servizio servizio) {
        this.contratto = new Contratto();
        this.contratto.setCliente(Objects.requireNonNull(cliente, "cliente"));
        this.contratto.setDipendente(Objects.requireNonNull(dipendente, "dipendente"));
        this.contratto.setServizio(Objects.requireNonNull(servizio, "servizio"));
    }

    private ContrattoBuilder(Contratto contratto) {
        this.contratto = contratto;
    }

    public static ContrattoBuilder nuovoContratto(Cliente cliente, Dipendente dipendente, Servizio servizio) {
        return new ContrattoBuilder(cliente, dipendente, servizio);
    }

    public static ContrattoBuilder daPrototype(Contratto contratto) {
        Objects.requireNonNull(contratto, "contratto");
        Contratto copia = new Contratto();
        copia.setCliente(contratto.getCliente());
        copia.setDipendente(contratto.getDipendente());
        copia.setServizio(contratto.getServizio());
        copia.setDataInizio(contratto.getDataInizio());
        copia.setDataFine(contratto.getDataFine());
        copia.setImporto(contratto.getImporto());
        copia.setStato(contratto.getStato());
        copia.setNote(contratto.getNote());
        return new ContrattoBuilder(copia);
    }

    public ContrattoBuilder conCliente(Cliente cliente) {
        this.contratto.setCliente(Objects.requireNonNull(cliente, "cliente"));
        return this;
    }

    public ContrattoBuilder conDipendente(Dipendente dipendente) {
        this.contratto.setDipendente(Objects.requireNonNull(dipendente, "dipendente"));
        return this;
    }

    public ContrattoBuilder conServizio(Servizio servizio) {
        this.contratto.setServizio(Objects.requireNonNull(servizio, "servizio"));
        return this;
    }

    public ContrattoBuilder conDataInizio(LocalDate dataInizio) {
        this.contratto.setDataInizio(Objects.requireNonNull(dataInizio, "dataInizio"));
        if (contratto.getDataFine() != null && contratto.getDataFine().isBefore(dataInizio)) {
            throw new IllegalArgumentException("La data di fine non può precedere la data di inizio");
        }
        return this;
    }

    public ContrattoBuilder conDataFine(LocalDate dataFine) {
        if (dataFine != null && contratto.getDataInizio() != null && dataFine.isBefore(contratto.getDataInizio())) {
            throw new IllegalArgumentException("La data di fine non può precedere la data di inizio");
        }
        this.contratto.setDataFine(dataFine);
        return this;
    }

    public ContrattoBuilder conPeriodo(LocalDate dataInizio, LocalDate dataFine) {
        conDataInizio(Objects.requireNonNull(dataInizio, "dataInizio"));
        conDataFine(dataFine);
        return this;
    }

    public ContrattoBuilder conImporto(BigDecimal importo) {
        Objects.requireNonNull(importo, "importo");
        if (importo.signum() <= 0) {
            throw new IllegalArgumentException("L'importo deve essere positivo");
        }
        this.contratto.setImporto(importo);
        return this;
    }

    public ContrattoBuilder conStato(String stato) {
        this.contratto.setStato(stato);
        return this;
    }

    public ContrattoBuilder conNote(String note) {
        this.contratto.setNote(note);
        return this;
    }

    public Contratto build() {
        Objects.requireNonNull(contratto.getCliente(), "Cliente obbligatorio");
        Objects.requireNonNull(contratto.getDipendente(), "Dipendente obbligatorio");
        Objects.requireNonNull(contratto.getServizio(), "Servizio obbligatorio");
        Objects.requireNonNull(contratto.getDataInizio(), "Data inizio obbligatoria");
        Objects.requireNonNull(contratto.getImporto(), "Importo obbligatorio");
        if (contratto.getDataFine() != null && contratto.getDataFine().isBefore(contratto.getDataInizio())) {
            throw new IllegalStateException("La data di fine deve essere successiva o uguale alla data di inizio");
        }
        if (contratto.getImporto().signum() <= 0) {
            throw new IllegalStateException("L'importo deve essere positivo");
        }
        return contratto;
    }
}
