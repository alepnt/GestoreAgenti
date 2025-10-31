package com.example.GestoreAgenti.service.facade;

import java.util.List;

import com.example.GestoreAgenti.model.composite.TeamComponent;

/**
 * Rappresentazione serializzabile della gerarchia dei team. Il nodo viene
 * costruito a partire dalla struttura Composite e mantiene solo le informazioni
 * necessarie all'esposizione via REST.
 */
public record TeamHierarchyNode(String name, int members, List<TeamHierarchyNode> children) {

    /**
     * Costruisce ricorsivamente un nodo a partire dal componente Composite.
     *
     * @param component componente sorgente
     * @return nodo pronto per la serializzazione
     */
    public static TeamHierarchyNode fromComponent(TeamComponent component) {
        List<TeamHierarchyNode> childNodes = component.getChildren().stream()
                .map(TeamHierarchyNode::fromComponent)
                .toList();
        return new TeamHierarchyNode(component.getName(), component.countMembers(), childNodes);
    }
}
