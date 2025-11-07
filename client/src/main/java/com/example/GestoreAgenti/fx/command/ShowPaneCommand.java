package com.example.GestoreAgenti.fx.command; // Esegue: package com.example.GestoreAgenti.fx.command;

import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.function.Consumer; // Esegue: import java.util.function.Consumer;

import javafx.scene.layout.AnchorPane; // Esegue: import javafx.scene.layout.AnchorPane;

public class ShowPaneCommand implements Command { // Esegue: public class ShowPaneCommand implements Command {

    private final Consumer<AnchorPane> showPaneAction; // Esegue: private final Consumer<AnchorPane> showPaneAction;
    private final AnchorPane pane; // Esegue: private final AnchorPane pane;

    public ShowPaneCommand(Consumer<AnchorPane> showPaneAction, AnchorPane pane) { // Esegue: public ShowPaneCommand(Consumer<AnchorPane> showPaneAction, AnchorPane pane) {
        this.showPaneAction = Objects.requireNonNull(showPaneAction, "showPaneAction"); // Esegue: this.showPaneAction = Objects.requireNonNull(showPaneAction, "showPaneAction");
        this.pane = Objects.requireNonNull(pane, "pane"); // Esegue: this.pane = Objects.requireNonNull(pane, "pane");
    } // Esegue: }

    @Override // Esegue: @Override
    public void execute() { // Esegue: public void execute() {
        showPaneAction.accept(pane); // Esegue: showPaneAction.accept(pane);
    } // Esegue: }
} // Esegue: }
