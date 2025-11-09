package com.example.GestoreAgenti.service.facade; // Definisce il pacchetto com.example.GestoreAgenti.service.facade che contiene questa classe.

import java.util.LinkedHashMap; // Importa java.util.LinkedHashMap per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.Map; // Importa java.util.Map per abilitare le funzionalità utilizzate nel file.
import java.util.Optional; // Importa java.util.Optional per abilitare le funzionalità utilizzate nel file.

import org.springframework.stereotype.Component; // Importa org.springframework.stereotype.Component per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.Team; // Importa com.example.GestoreAgenti.model.Team per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.composite.AgentLeaf; // Importa com.example.GestoreAgenti.model.composite.AgentLeaf per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.composite.TeamComponent; // Importa com.example.GestoreAgenti.model.composite.TeamComponent per abilitare le funzionalità utilizzate nel file.
import com.example.GestoreAgenti.model.composite.TeamComposite; // Importa com.example.GestoreAgenti.model.composite.TeamComposite per abilitare le funzionalità utilizzate nel file.

/**
 * Costruisce la gerarchia dei team partendo dalle entità del dominio e
 * restituendo una struttura Composite navigabile.
 */
@Component // Applica l'annotazione @Component per configurare il componente.
public class TeamHierarchyBuilder { // Definisce la classe TeamHierarchyBuilder che incapsula la logica applicativa.

    public TeamComponent buildHierarchy(List<Team> teams, List<Dipendente> agents) { // Definisce il metodo buildHierarchy che supporta la logica di dominio.
        TeamComposite root = new TeamComposite("Organizzazione"); // Assegna il valore calcolato alla variabile TeamComposite root.
        Map<String, TeamComposite> compositeByTeam = new LinkedHashMap<>(); // Assegna il valore calcolato alla variabile Map<String, TeamComposite> compositeByTeam.
        compositeByTeam.put(root.getName(), root); // Esegue l'istruzione terminata dal punto e virgola.

        for (Team team : teams) { // Itera sugli elementi richiesti dalla logica.
            String name = Optional.ofNullable(team.getProvincia()).filter(s -> !s.isBlank()) // Esegue l'istruzione necessaria alla logica applicativa.
                    .orElse("Team " + Optional.ofNullable(team.getNo()).map(Object::toString).orElse("Sconosciuto")); // Esegue l'istruzione terminata dal punto e virgola.
            TeamComposite composite = new TeamComposite(name); // Assegna il valore calcolato alla variabile TeamComposite composite.
            root.addChild(composite); // Esegue l'istruzione terminata dal punto e virgola.
            compositeByTeam.put(name, composite); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.

        for (Dipendente agent : agents) { // Itera sugli elementi richiesti dalla logica.
            String teamName = Optional.ofNullable(agent.getTeam()).filter(s -> !s.isBlank()).orElse("Senza Team"); // Assegna il valore calcolato alla variabile String teamName.
            TeamComposite composite = compositeByTeam.computeIfAbsent(teamName, key -> { // Apre il blocco di codice associato alla dichiarazione.
                TeamComposite extra = new TeamComposite(key); // Assegna il valore calcolato alla variabile TeamComposite extra.
                root.addChild(extra); // Esegue l'istruzione terminata dal punto e virgola.
                return extra; // Restituisce il risultato dell'espressione extra.
            }); // Chiude il blocco di codice precedente.
            composite.addChild(new AgentLeaf(agent)); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.

        return root; // Restituisce il risultato dell'espressione root.
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
