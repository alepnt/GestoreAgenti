package com.example.GestoreAgenti.model.composite; // Definisce il pacchetto com.example.GestoreAgenti.model.composite che contiene questa classe.

import java.util.ArrayList; // Importa java.util.ArrayList per abilitare le funzionalità utilizzate nel file.
import java.util.Collections; // Importa java.util.Collections per abilitare le funzionalità utilizzate nel file.
import java.util.List; // Importa java.util.List per abilitare le funzionalità utilizzate nel file.
import java.util.stream.Collectors; // Importa java.util.stream.Collectors per abilitare le funzionalità utilizzate nel file.

import com.example.GestoreAgenti.model.Dipendente; // Importa com.example.GestoreAgenti.model.Dipendente per abilitare le funzionalità utilizzate nel file.

/**
 * Nodo composito che rappresenta un gruppo di agenti o un team.
 */
public class TeamComposite implements TeamComponent { // Definisce la classe TeamComposite che incapsula la logica applicativa.

    private final String name; // Dichiara il campo name dell'oggetto.
    private final List<TeamComponent> children = new ArrayList<>(); // Definisce il metodo ArrayList<> che supporta la logica di dominio.

    public TeamComposite(String name) { // Costruttore della classe TeamComposite che inizializza le dipendenze necessarie.
        this.name = name == null || name.isBlank() ? "Team" : name; // Aggiorna il campo name dell'istanza.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public String getName() { // Definisce il metodo getName che supporta la logica di dominio.
        return name; // Restituisce il risultato dell'espressione name.
    } // Chiude il blocco di codice precedente.

    public void addChild(TeamComponent component) { // Definisce il metodo addChild che supporta la logica di dominio.
        if (component != null) { // Valuta la condizione per controllare il flusso applicativo.
            children.add(component); // Esegue l'istruzione terminata dal punto e virgola.
        } // Chiude il blocco di codice precedente.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public int countMembers() { // Definisce il metodo countMembers che supporta la logica di dominio.
        return children.stream().mapToInt(TeamComponent::countMembers).sum(); // Restituisce il risultato dell'espressione children.stream().mapToInt(TeamComponent::countMembers).sum().
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public List<Dipendente> getAgents() { // Definisce il metodo getAgents che supporta la logica di dominio.
        return children.stream() // Restituisce il risultato dell'espressione children.stream().
                .flatMap(component -> component.getAgents().stream()) // Esegue l'istruzione necessaria alla logica applicativa.
                .collect(Collectors.toUnmodifiableList()); // Esegue l'istruzione terminata dal punto e virgola.
    } // Chiude il blocco di codice precedente.

    @Override // Applica l'annotazione @Override per configurare il componente.
    public List<TeamComponent> getChildren() { // Definisce il metodo getChildren che supporta la logica di dominio.
        return Collections.unmodifiableList(children); // Restituisce il risultato dell'espressione Collections.unmodifiableList(children).
    } // Chiude il blocco di codice precedente.
} // Chiude il blocco di codice precedente.
