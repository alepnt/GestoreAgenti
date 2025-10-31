package com.example.GestoreAgenti.fx.command;

import java.util.Objects;
import java.util.function.Consumer;

import javafx.scene.layout.AnchorPane;

public class ShowPaneCommand implements Command {

    private final Consumer<AnchorPane> showPaneAction;
    private final AnchorPane pane;

    public ShowPaneCommand(Consumer<AnchorPane> showPaneAction, AnchorPane pane) {
        this.showPaneAction = Objects.requireNonNull(showPaneAction, "showPaneAction");
        this.pane = Objects.requireNonNull(pane, "pane");
    }

    @Override
    public void execute() {
        showPaneAction.accept(pane);
    }
}
