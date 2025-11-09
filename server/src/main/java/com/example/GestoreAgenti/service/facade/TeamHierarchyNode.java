package com.example.GestoreAgenti.service.facade; // Definisce il pacchetto com.example.GestoreAgenti.service.facade che contiene questa classe.

import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.composite.TeamComponent; // Importa com.example.GestoreAgenti.model.composite.TeamComponent per abilitare le funzionalità utilizzate nel file.

/**
 * Rappresentazione serializzabile della gerarchia dei team. Il nodo viene
 * costruito a partire dalla struttura Composite e mantiene solo le informazioni
 * necessarie all'esposizione via REST.
 */
public record TeamHierarchyNode(String name, int members, List<TeamHierarchyNode> children) { // Definisce la record TeamHierarchyNode che incapsula la logica applicativa.

    /**
     * Costruisce ricorsivamente un nodo a partire dal componente Composite.
     *
     * @param component componente sorgente
     * @return nodo pronto per la serializzazione
     */
    public static TeamHierarchyNode fromComponent(TeamComponent component) { // Definisce il metodo fromComponent che supporta la logica di dominio.
        List<TeamHierarchyNode> childNodes = component.getChildren().stream() // Esegue l'istruzione necessaria alla logica applicativa.
                .map(TeamHierarchyNode::fromComponent) // Esegue l'istruzione necessaria alla logica applicativa.
                .toList(); // Esegue l'istruzione terminata dal punto e virgola.
        return new TeamHierarchyNode(component.getName(), component.countMembers(), childNodes); // Restituisce il risultato dell'espressione new TeamHierarchyNode(component.getName(), component.countMembers(), childNodes).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
