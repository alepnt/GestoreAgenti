package com.example.GestoreAgenti.fx.command;

import java.util.Objects;
import java.util.function.Supplier;

import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.Employee;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class SendTeamMessageCommand implements Command {

    private final Supplier<String> messageSupplier;
    private final FxDataService dataService;
    private final Employee employee;
    private final TextArea input;
    private final ListView<?> listView;

    public SendTeamMessageCommand(Supplier<String> messageSupplier,
                                  FxDataService dataService,
                                  Employee employee,
                                  TextArea input,
                                  ListView<?> listView) {
        this.messageSupplier = Objects.requireNonNull(messageSupplier, "messageSupplier");
        this.dataService = Objects.requireNonNull(dataService, "dataService");
        this.employee = Objects.requireNonNull(employee, "employee");
        this.input = Objects.requireNonNull(input, "input");
        this.listView = Objects.requireNonNull(listView, "listView");
    }

    @Override
    public void execute() {
        String message = messageSupplier.get();
        if (message == null || message.isBlank()) {
            return;
        }
        dataService.sendTeamMessage(employee, message);
        input.clear();
        int lastIndex = listView.getItems().size() - 1;
        if (lastIndex >= 0) {
            listView.scrollTo(lastIndex);
        }
    }
}
