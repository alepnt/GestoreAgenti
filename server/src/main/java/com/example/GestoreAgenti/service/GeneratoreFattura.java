package com.example.GestoreAgenti.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Fattura;

/**
 * Singleton che incapsula la logica di generazione delle fatture a partire da contratti.
 */
public final class GeneratoreFattura {

    private static final GeneratoreFattura INSTANCE = new GeneratoreFattura();

    private GeneratoreFattura() {
    }

    public static GeneratoreFattura getInstance() {
        return INSTANCE;
    }

    public Fattura creaDaContratto(Contratto contratto, String numeroFattura, BigDecimal aliquotaIva) {
        return creaDaContratto(contratto, numeroFattura, LocalDate.now(), aliquotaIva);
    }

    public Fattura creaDaContratto(Contratto contratto, String numeroFattura, LocalDate dataEmissione, BigDecimal aliquotaIva) {
        Objects.requireNonNull(contratto, "contratto");
        Objects.requireNonNull(numeroFattura, "numeroFattura");
        Objects.requireNonNull(dataEmissione, "dataEmissione");
        Objects.requireNonNull(aliquotaIva, "aliquotaIva");
        if (contratto.getCliente() == null) {
            throw new IllegalArgumentException("Il contratto deve avere un cliente associato");
        }
        if (contratto.getImporto() == null) {
            throw new IllegalArgumentException("Il contratto deve avere un importo per generare una fattura");
        }

        return Fattura.builder(contratto)
                .conNumero(numeroFattura)
                .emessaIl(dataEmissione)
                .conImponibile(contratto.getImporto())
                .conAliquotaIva(aliquotaIva)
                .build();
    }
}
