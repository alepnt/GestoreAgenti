package com.example.GestoreAgenti.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.model.Pagamento;
import com.example.GestoreAgenti.model.Provvigione;
import com.example.GestoreAgenti.model.Team;
import com.example.GestoreAgenti.model.commission.CommissionBase;
import com.example.GestoreAgenti.model.commission.CommissionDistributionMode;
import com.example.GestoreAgenti.repository.ProvvigioneRepository;
import com.example.GestoreAgenti.repository.TeamRepository;

@Service
public class CommissionCalculationService {

    private static final BigDecimal TOLLERANZA = new BigDecimal("0.0001");

    private final TeamRepository teamRepository;
    private final DipendenteService dipendenteService;
    private final ProvvigioneRepository provvigioneRepository;

    public CommissionCalculationService(TeamRepository teamRepository,
                                        DipendenteService dipendenteService,
                                        ProvvigioneRepository provvigioneRepository) {
        this.teamRepository = teamRepository;
        this.dipendenteService = dipendenteService;
        this.provvigioneRepository = provvigioneRepository;
    }

    @Transactional
    public List<Provvigione> calcolaProvvigioni(Fattura fattura) {
        if (fattura == null || fattura.getContratto() == null) {
            return List.of();
        }
        Contratto contratto = fattura.getContratto();
        Dipendente referente = contratto.getDipendente();
        if (referente == null || referente.getTeam() == null) {
            return List.of();
        }

        Team team = teamRepository.findByProvinciaIgnoreCase(referente.getTeam())
                .orElseThrow(() -> new IllegalStateException(
                        "Team non configurato per la provincia " + referente.getTeam()));

        if (team.getPercentualeProvvigione() == null) {
            throw new IllegalStateException("Il team " + team.getProvincia() + " non ha una percentuale di provvigione definita");
        }

        List<Dipendente> membri = new ArrayList<>(dipendenteService.findByTeam(team.getProvincia()));
        membri.sort(Comparator
                .comparing((Dipendente d) -> d.getOrdineRanking() == null ? Integer.MAX_VALUE : d.getOrdineRanking())
                .thenComparing(d -> d.getId() == null ? Long.MAX_VALUE : d.getId()));

        BigDecimal teamPercent = team.getPercentualeProvvigione();
        if (team.getDistribuzioneProvvigioni() == CommissionDistributionMode.PERCENTUALE) {
            BigDecimal somma = membri.stream()
                    .map(d -> d.getCommissionePercentuale() == null ? BigDecimal.ZERO : d.getCommissionePercentuale())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (somma.subtract(teamPercent).abs().compareTo(TOLLERANZA) > 0) {
                throw new IllegalStateException("La somma delle percentuali del team " + team.getProvincia()
                        + " non corrisponde alla quota complessiva prevista");
            }
        }

        BigDecimal baseImporto = resolveBaseAmount(fattura, team);
        if (baseImporto == null) {
            return List.of();
        }

        BigDecimal residuo = teamPercent;
        List<Provvigione> risultati = new ArrayList<>();

        for (Dipendente membro : membri) {
            BigDecimal quotaMembro = membro.getCommissionePercentuale();
            if (quotaMembro == null || quotaMembro.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal percentualeApplicata;
            if (team.getDistribuzioneProvvigioni() == CommissionDistributionMode.SBARRAMENTO) {
                if (residuo.compareTo(BigDecimal.ZERO) <= 0) {
                    break;
                }
                percentualeApplicata = quotaMembro.min(residuo);
                residuo = residuo.subtract(percentualeApplicata);
            } else {
                percentualeApplicata = quotaMembro;
            }

            if (percentualeApplicata.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            BigDecimal importo = baseImporto.multiply(percentualeApplicata)
                    .setScale(2, RoundingMode.HALF_UP);

            Provvigione provvigione = provvigioneRepository
                    .findByFattura_IdFatturaAndDipendente_Id(fattura.getIdFattura(), membro.getId())
                    .orElseGet(Provvigione::new);
            provvigione.setDipendente(membro);
            provvigione.setContratto(contratto);
            provvigione.setFattura(fattura);
            provvigione.setPercentuale(percentualeApplicata.setScale(4, RoundingMode.HALF_UP));
            provvigione.setImporto(importo);
            provvigione.setDataCalcolo(LocalDate.now());
            provvigione.setStato(contratto.isProvvigioneAllaFirma() ? "EROGATA" : "IN_ATTESA");
            risultati.add(provvigioneRepository.save(provvigione));
        }

        return risultati;
    }

    @Transactional
    public void consolidaProvvigioni(Pagamento pagamento, String nuovoStato) {
        if (pagamento == null || pagamento.getFattura() == null
                || pagamento.getFattura().getIdFattura() == null) {
            return;
        }
        if (!Objects.equals(nuovoStato, com.example.GestoreAgenti.model.state.pagamento.CompletatoState.NOME)) {
            return;
        }
        List<Provvigione> provvigioni = provvigioneRepository
                .findByFattura_IdFattura(pagamento.getFattura().getIdFattura());
        for (Provvigione provvigione : provvigioni) {
            if (!"EROGATA".equalsIgnoreCase(provvigione.getStato())) {
                provvigione.setStato("EROGATA");
                provvigione.setDataCalcolo(LocalDate.now());
                provvigioneRepository.save(provvigione);
            }
        }
    }

    private BigDecimal resolveBaseAmount(Fattura fattura, Team team) {
        if (team.getBaseCalcolo() == CommissionBase.TOTALE) {
            return defaultAmount(fattura.getTotale());
        }
        return defaultAmount(fattura.getImponibile());
    }

    private BigDecimal defaultAmount(BigDecimal value) {
        if (value == null) {
            return null;
        }
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
