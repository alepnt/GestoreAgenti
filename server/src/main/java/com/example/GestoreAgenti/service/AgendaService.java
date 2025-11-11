package com.example.GestoreAgenti.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.agenda.AgendaEvento;
import com.example.GestoreAgenti.model.agenda.AgendaItemType;
import com.example.GestoreAgenti.repository.AgendaEventoRepository;
import com.example.GestoreAgenti.repository.DipendenteRepository;

@Service
@Transactional
public class AgendaService {

    private final AgendaEventoRepository repository;
    private final DipendenteRepository dipendenteRepository;

    public AgendaService(AgendaEventoRepository repository, DipendenteRepository dipendenteRepository) {
        this.repository = repository;
        this.dipendenteRepository = dipendenteRepository;
    }

    public List<AgendaEvento> listForDipendente(Long dipendenteId, LocalDate from, LocalDate to) {
        Objects.requireNonNull(dipendenteId, "L'identificativo del dipendente è obbligatorio");
        if (from != null && to != null) {
            return repository.findByDipendente_IdAndDataBetweenOrderByDataAscOraInizioAsc(dipendenteId, from, to);
        }
        return repository.findByDipendente_IdOrderByDataAscOraInizioAsc(dipendenteId);
    }

    public AgendaEvento create(Long dipendenteId, AgendaEvento evento) {
        Objects.requireNonNull(dipendenteId, "L'identificativo del dipendente è obbligatorio");
        Objects.requireNonNull(evento, "L'evento da creare è obbligatorio");
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new IllegalArgumentException("Dipendente non trovato: " + dipendenteId));
        evento.setDipendente(dipendente);
        return repository.save(evento);
    }

    public AgendaEvento update(Long id, AgendaEvento changes, AgendaItemType requestedType) {
        Objects.requireNonNull(id, "L'identificativo dell'evento è obbligatorio");
        Objects.requireNonNull(changes, "I dati aggiornati dell'evento sono obbligatori");
        AgendaEvento existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento agenda non trovato: " + id));
        existing.setData(changes.getData());
        existing.setOraInizio(changes.getOraInizio());
        existing.setOraFine(changes.getOraFine());
        existing.setTitolo(changes.getTitolo());
        existing.setDescrizione(changes.getDescrizione());
        if (requestedType != null) {
            existing.setTipo(requestedType);
        }
        existing.setCompletato(changes.isCompletato());
        return existing;
    }

    public void delete(Long id) {
        Objects.requireNonNull(id, "L'identificativo dell'evento è obbligatorio");
        repository.deleteById(id);
    }
}
