package com.example.GestoreAgenti.model.builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Fattura;

/**
 * Builder per creare istanze di {@link Fattura} applicando le regole di dominio
 * su importi e stato iniziale.
 */
public final class FatturaBuilder {

    private final Fattura fattura;
    private BigDecimal aliquotaIva;

    private FatturaBuilder(Cliente cliente, Contratto contratto) {
        this.fattura = new Fattura();
        this.fattura.setCliente(Objects.requireNonNull(cliente, "cliente"));
        this.fattura.setContratto(Objects.requireNonNull(contratto, "contratto"));
    }

    private FatturaBuilder(Fattura fattura) {
        this.fattura = fattura;
    }

    /**
     * Crea un builder per una nuova fattura partendo da cliente e contratto.
     */
    public static FatturaBuilder nuovaFattura(Cliente cliente, Contratto contratto) {
        return new FatturaBuilder(cliente, contratto);
    }

    /**
     * Crea un builder inizializzando cliente e importo a partire dal contratto.
     */
    public static FatturaBuilder perContratto(Contratto contratto) {
        Objects.requireNonNull(contratto, "contratto");
        Cliente cliente = Objects.requireNonNull(contratto.getCliente(), "Il contratto deve avere un cliente associato");
        FatturaBuilder builder = new FatturaBuilder(cliente, contratto);
        if (contratto.getImporto() != null) {
            builder.conImponibile(contratto.getImporto());
        }
        builder.fattura.setNumeroFattura(contratto.getIdContratto() != null
                ? "CTR-" + contratto.getIdContratto()
                : null);
        builder.fattura.setDataEmissione(contratto.getDataFine() != null ? contratto.getDataFine() : LocalDate.now());
        return builder;
    }

    /**
     * Popola il builder a partire da una fattura esistente.
     */
    public static FatturaBuilder daPrototype(Fattura fattura) {
        Objects.requireNonNull(fattura, "fattura");
        Fattura copia = new Fattura();
        copia.setCliente(fattura.getCliente());
        copia.setContratto(fattura.getContratto());
        copia.setNumeroFattura(fattura.getNumeroFattura());
        copia.setDataEmissione(fattura.getDataEmissione());
        copia.setImponibile(fattura.getImponibile());
        copia.setIva(fattura.getIva());
        copia.setTotale(fattura.getTotale());
        copia.setState(fattura.getState());
        return new FatturaBuilder(copia);
    }

    public FatturaBuilder conNumero(String numeroFattura) {
        this.fattura.setNumeroFattura(Objects.requireNonNull(numeroFattura, "numeroFattura"));
        return this;
    }

    public FatturaBuilder emessaIl(LocalDate dataEmissione) {
        this.fattura.setDataEmissione(Objects.requireNonNull(dataEmissione, "dataEmissione"));
        return this;
    }

    public FatturaBuilder conImponibile(BigDecimal imponibile) {
        Objects.requireNonNull(imponibile, "imponibile");
        if (imponibile.signum() < 0) {
            throw new IllegalArgumentException("L'imponibile deve essere positivo");
        }
        this.fattura.setImponibile(imponibile);
        ricalcolaTotali();
        return this;
    }

    public FatturaBuilder conIva(BigDecimal iva) {
        Objects.requireNonNull(iva, "iva");
        if (iva.signum() < 0) {
            throw new IllegalArgumentException("L'IVA deve essere positiva");
        }
        this.fattura.setIva(iva);
        ricalcolaTotali();
        return this;
    }

    public FatturaBuilder conAliquotaIva(BigDecimal aliquota) {
        Objects.requireNonNull(aliquota, "aliquota");
        if (aliquota.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("L'aliquota IVA non può essere negativa");
        }
        this.aliquotaIva = aliquota;
        ricalcolaTotali();
        return this;
    }

    public FatturaBuilder conTotale(BigDecimal totale) {
        Objects.requireNonNull(totale, "totale");
        if (totale.signum() < 0) {
            throw new IllegalArgumentException("Il totale deve essere positivo");
        }
        this.fattura.setTotale(totale);
        return this;
    }

    private void ricalcolaTotali() {
        if (fattura.getImponibile() == null) {
            return;
        }
        if (aliquotaIva != null) {
            BigDecimal ivaCalcolata = fattura.getImponibile().multiply(aliquotaIva).setScale(2, RoundingMode.HALF_UP);
            fattura.setIva(ivaCalcolata);
            fattura.setTotale(fattura.getImponibile().add(ivaCalcolata));
        } else if (fattura.getIva() != null) {
            fattura.setTotale(fattura.getImponibile().add(fattura.getIva()));
        }
    }

    public Fattura build() {
        Objects.requireNonNull(fattura.getNumeroFattura(), "Numero fattura obbligatorio");
        Objects.requireNonNull(fattura.getDataEmissione(), "Data emissione obbligatoria");
        Objects.requireNonNull(fattura.getImponibile(), "Imponibile obbligatorio");
        if (fattura.getIva() == null && aliquotaIva != null) {
            ricalcolaTotali();
        }
        Objects.requireNonNull(fattura.getIva(), "IVA obbligatoria");
        if (fattura.getTotale() == null) {
            fattura.setTotale(fattura.getImponibile().add(fattura.getIva()));
        }
        BigDecimal atteso = fattura.getImponibile().add(fattura.getIva());
        if (fattura.getTotale().compareTo(atteso) != 0) {
            throw new IllegalStateException("Totale incoerente con imponibile e IVA");
        }
        return fattura;
    }
}
