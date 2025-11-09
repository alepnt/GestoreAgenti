package com.example.GestoreAgenti.service.facade; // Definisce il pacchetto com.example.GestoreAgenti.service.facade che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Cliente; // Importa com.example.GestoreAgenti.model.Cliente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Contratto; // Importa com.example.GestoreAgenti.model.Contratto per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Provvigione; // Importa com.example.GestoreAgenti.model.Provvigione per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Team; // Importa com.example.GestoreAgenti.model.Team per abilitare le funzionalità utilizzate nel file.

/**
 * DTO read-only che raccoglie i dati significativi di un agente.
 * La facciata aggrega le diverse entità del dominio per fornire al controller
 * una vista coerente senza esporre direttamente la logica di orchestrazione.
 */
public record AgentOverview(Dipendente dipendente, // Definisce la record AgentOverview che incapsula la logica applicativa.
                            List<Cliente> clienti, // Esegue l'istruzione necessaria alla logica applicativa.
                            List<Contratto> contratti, // Esegue l'istruzione necessaria alla logica applicativa.
                            List<Provvigione> provvigioni, // Esegue l'istruzione necessaria alla logica applicativa.
                            Team team) { // Apre il blocco di codice associato alla dichiarazione.
} // Chiude il blocco di codice precedente.
