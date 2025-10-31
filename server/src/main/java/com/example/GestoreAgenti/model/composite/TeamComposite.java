package com.example.GestoreAgenti.model.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.example.GestoreAgenti.model.Dipendente;

/**
 * Nodo composito che rappresenta un gruppo di agenti o un team.
 */
public class TeamComposite implements TeamComponent {

    private final String name;
    private final List<TeamComponent> children = new ArrayList<>();

    public TeamComposite(String name) {
        this.name = name == null || name.isBlank() ? "Team" : name;
    }

    @Override
    public String getName() {
        return name;
    }

    public void addChild(TeamComponent component) {
        if (component != null) {
            children.add(component);
        }
    }

    @Override
    public int countMembers() {
        return children.stream().mapToInt(TeamComponent::countMembers).sum();
    }

    @Override
    public List<Dipendente> getAgents() {
        return children.stream()
                .flatMap(component -> component.getAgents().stream())
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<TeamComponent> getChildren() {
        return Collections.unmodifiableList(children);
    }
}
