package com.example.GestoreAgenti.service.facade;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.GestoreAgenti.model.Dipendente;
import com.example.GestoreAgenti.model.Team;
import com.example.GestoreAgenti.model.composite.AgentLeaf;
import com.example.GestoreAgenti.model.composite.TeamComponent;
import com.example.GestoreAgenti.model.composite.TeamComposite;

/**
 * Costruisce la gerarchia dei team partendo dalle entità del dominio e
 * restituendo una struttura Composite navigabile.
 */
@Component
public class TeamHierarchyBuilder {

    public TeamComponent buildHierarchy(List<Team> teams, List<Dipendente> agents) {
        TeamComposite root = new TeamComposite("Organizzazione");
        Map<String, TeamComposite> compositeByTeam = new LinkedHashMap<>();
        compositeByTeam.put(root.getName(), root);

        for (Team team : teams) {
            String name = Optional.ofNullable(team.getProvincia()).filter(s -> !s.isBlank())
                    .orElse("Team " + Optional.ofNullable(team.getNo()).map(Object::toString).orElse("Sconosciuto"));
            TeamComposite composite = new TeamComposite(name);
            root.addChild(composite);
            compositeByTeam.put(name, composite);
        }

        for (Dipendente agent : agents) {
            String teamName = Optional.ofNullable(agent.getTeam()).filter(s -> !s.isBlank()).orElse("Senza Team");
            TeamComposite composite = compositeByTeam.computeIfAbsent(teamName, key -> {
                TeamComposite extra = new TeamComposite(key);
                root.addChild(extra);
                return extra;
            });
            composite.addChild(new AgentLeaf(agent));
        }

        return root;
    }
}
