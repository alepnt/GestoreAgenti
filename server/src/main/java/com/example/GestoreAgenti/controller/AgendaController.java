package com.example.GestoreAgenti.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.GestoreAgenti.model.agenda.AgendaEvento;
import com.example.GestoreAgenti.model.agenda.AgendaItemType;
import com.example.GestoreAgenti.service.AgendaService;

@RestController
@RequestMapping("/api/agenda")
public class AgendaController {

    private final AgendaService agendaService;

    public AgendaController(AgendaService agendaService) {
        this.agendaService = agendaService;
    }

    @GetMapping("/dipendenti/{dipendenteId}")
    public List<AgendaItemResponse> listEvents(@PathVariable Long dipendenteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return agendaService.listForDipendente(dipendenteId, from, to).stream()
                .map(AgendaController::toResponse)
                .collect(Collectors.toList());
    }

    @PostMapping("/dipendenti/{dipendenteId}")
    @ResponseStatus(HttpStatus.CREATED)
    public AgendaItemResponse create(@PathVariable Long dipendenteId, @RequestBody AgendaItemRequest request) {
        AgendaEvento evento = request.toEntity();
        AgendaItemType requestedType = parseType(request.type());
        if (requestedType != null) {
            evento.setTipo(requestedType);
        }
        return toResponse(agendaService.create(dipendenteId, evento));
    }

    @PutMapping("/{id}")
    public AgendaItemResponse update(@PathVariable Long id, @RequestBody AgendaItemRequest request) {
        AgendaEvento payload = request.toEntity();
        AgendaItemType requestedType = parseType(request.type());
        AgendaEvento updated = agendaService.update(id, payload, requestedType);
        return toResponse(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        agendaService.delete(id);
    }

    private static AgendaItemResponse toResponse(AgendaEvento evento) {
        LocalDate date = evento.getData();
        LocalTime startTime = evento.getOraInizio() != null ? evento.getOraInizio() : LocalTime.of(9, 0);
        LocalTime endTime = evento.getOraFine() != null ? evento.getOraFine() : startTime.plusHours(1);
        LocalDateTime start = date != null ? LocalDateTime.of(date, startTime) : null;
        LocalDateTime end = date != null ? LocalDateTime.of(date, endTime) : null;
        return new AgendaItemResponse(evento.getId(),
                evento.getDipendente() != null ? evento.getDipendente().getId() : null,
                evento.getTitolo(), evento.getDescrizione(), evento.getTipo().name(),
                start, end, evento.isCompletato());
    }

    public record AgendaItemRequest(LocalDate date, LocalTime startTime, LocalTime endTime,
            String title, String description, String type, boolean completed) {

        AgendaEvento toEntity() {
            AgendaEvento evento = new AgendaEvento();
            evento.setData(date);
            evento.setOraInizio(startTime);
            evento.setOraFine(endTime);
            evento.setTitolo(title);
            evento.setDescrizione(description);
            evento.setCompletato(completed);
            return evento;
        }
    }

    public record AgendaItemResponse(Long id, Long dipendenteId, String title, String description,
            String type, LocalDateTime start, LocalDateTime end, boolean completed) {
    }

    private static AgendaItemType parseType(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return AgendaItemType.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tipo evento agenda non valido: " + raw, ex);
        }
    }
}
