package com.example.GestoreAgenti.service.facade;

import java.util.List;

import com.example.GestoreAgenti.model.Cliente;
import com.example.GestoreAgenti.model.Contratto;
import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Provvigione;
import com.example.GestoreAgenti.model.Team;

/**
 * DTO read-only che raccoglie i dati significativi di un agente.
 * La facciata aggrega le diverse entità del dominio per fornire al controller
 * una vista coerente senza esporre direttamente la logica di orchestrazione.
 */
public record AgentOverview(Dipendente dipendente,
                            List<Cliente> clienti,
                            List<Contratto> contratti,
                            List<Provvigione> provvigioni,
                            Team team) {
}
