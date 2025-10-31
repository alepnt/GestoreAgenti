package com.example.GestoreAgenti.service; // Definisce il pacchetto com.example.GestoreAgenti.service a cui appartiene questa classe.

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.GestoreAgenti.model.Fattura;
import com.example.GestoreAgenti.model.state.AnnullataState;
import com.example.GestoreAgenti.model.state.BozzaState;
import com.example.GestoreAgenti.model.state.EmessaState;
import com.example.GestoreAgenti.model.state.PagataState;
import com.example.GestoreAgenti.repository.FatturaRepository;
import com.example.GestoreAgenti.service.crud.AbstractCrudService;
import com.example.GestoreAgenti.service.crud.BeanCopyCrudEntityHandler;

@Service
public class FatturaService extends AbstractCrudService<Fattura, Long> {

    private static final class FatturaCrudHandler extends BeanCopyCrudEntityHandler<Fattura> {

        private FatturaCrudHandler() {
            super("idFattura", "state");
        }

        @Override
        public Fattura prepareForCreate(Fattura entity) {
            aggiornaStato(entity, entity.getStato());
            return entity;
        }

        @Override
        public Fattura merge(Fattura existing, Fattura changes) {
            super.merge(existing, changes);
            aggiornaStato(existing, changes.getStato());
            return existing;
        }

        private void aggiornaStato(Fattura fattura, String statoRichiesto) {
            if (statoRichiesto == null || statoRichiesto.equalsIgnoreCase(fattura.getStato())) {
                return;
            }

            String statoNormalizzato = statoRichiesto.toUpperCase();
            if (PagataState.NOME.equals(statoNormalizzato)) {
                if (fattura.getStato().equalsIgnoreCase(BozzaState.NOME)) {
                    fattura.emetti();
                }
                fattura.paga();
                return;
            }

            if (EmessaState.NOME.equals(statoNormalizzato)) {
                fattura.emetti();
                return;
            }

            if (AnnullataState.NOME.equals(statoNormalizzato)) {
                fattura.annulla();
                return;
            }

            if (BozzaState.NOME.equals(statoNormalizzato)) {
                if (!fattura.getStato().equalsIgnoreCase(BozzaState.NOME)) {
                    throw new IllegalStateException("Impossibile tornare allo stato BOZZA da " + fattura.getStato());
                }
                return;
            }

            throw new IllegalArgumentException("Stato fattura sconosciuto: " + statoRichiesto);
        }
    }

    public FatturaService(FatturaRepository repository) {
        super(repository, new FatturaCrudHandler(), "Fattura");
    }

    public List<Fattura> getAllFatture() {
        return findAll();
    }

    public Optional<Fattura> getFatturaById(Long id) {
        return findOptionalById(id);
    }

    public Fattura createFattura(Fattura fattura) {
        return create(fattura);
    }

    public Fattura updateFattura(Long id, Fattura fatturaDetails) {
        return update(id, fatturaDetails);
    }

    public void deleteFattura(Long id) {
        delete(id);
    }

    public Fattura emettiFattura(Long id) {
        Fattura fattura = findRequiredById(id);
        fattura.emetti();
        return repository().save(fattura);
    }

    public Fattura pagaFattura(Long id) {
        Fattura fattura = findRequiredById(id);
        fattura.paga();
        return repository().save(fattura);
    }

    public Fattura annullaFattura(Long id) {
        Fattura fattura = findRequiredById(id);
        fattura.annulla();
        return repository().save(fattura);
    }
}

