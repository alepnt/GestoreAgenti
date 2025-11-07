package com.example.GestoreAgenti.fx.command; // Esegue: package com.example.GestoreAgenti.fx.command;

import java.util.Objects; // Esegue: import java.util.Objects;
import java.util.function.Supplier; // Esegue: import java.util.function.Supplier;

import com.example.GestoreAgenti.fx.data.FxDataService; // Esegue: import com.example.GestoreAgenti.fx.data.FxDataService;
import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;

import javafx.scene.control.ListView; // Esegue: import javafx.scene.control.ListView;
import javafx.scene.control.TextArea; // Esegue: import javafx.scene.control.TextArea;

public class SendTeamMessageCommand implements Command { // Esegue: public class SendTeamMessageCommand implements Command {

    private final Supplier<String> messageSupplier; // Esegue: private final Supplier<String> messageSupplier;
    private final FxDataService dataService; // Esegue: private final FxDataService dataService;
    private final Employee employee; // Esegue: private final Employee employee;
    private final TextArea input; // Esegue: private final TextArea input;
    private final ListView<?> listView; // Esegue: private final ListView<?> listView;

    public SendTeamMessageCommand(Supplier<String> messageSupplier, // Esegue: public SendTeamMessageCommand(Supplier<String> messageSupplier,
                                  FxDataService dataService, // Esegue: FxDataService dataService,
                                  Employee employee, // Esegue: Employee employee,
                                  TextArea input, // Esegue: TextArea input,
                                  ListView<?> listView) { // Esegue: ListView<?> listView) {
        this.messageSupplier = Objects.requireNonNull(messageSupplier, "messageSupplier"); // Esegue: this.messageSupplier = Objects.requireNonNull(messageSupplier, "messageSupplier");
        this.dataService = Objects.requireNonNull(dataService, "dataService"); // Esegue: this.dataService = Objects.requireNonNull(dataService, "dataService");
        this.employee = Objects.requireNonNull(employee, "employee"); // Esegue: this.employee = Objects.requireNonNull(employee, "employee");
        this.input = Objects.requireNonNull(input, "input"); // Esegue: this.input = Objects.requireNonNull(input, "input");
        this.listView = Objects.requireNonNull(listView, "listView"); // Esegue: this.listView = Objects.requireNonNull(listView, "listView");
    } // Esegue: }

    @Override // Esegue: @Override
    public void execute() { // Esegue: public void execute() {
        String message = messageSupplier.get(); // Esegue: String message = messageSupplier.get();
        if (message == null || message.isBlank()) { // Esegue: if (message == null || message.isBlank()) {
            return; // Esegue: return;
        } // Esegue: }
        dataService.sendTeamMessage(employee, message); // Esegue: dataService.sendTeamMessage(employee, message);
        input.clear(); // Esegue: input.clear();
        int lastIndex = listView.getItems().size() - 1; // Esegue: int lastIndex = listView.getItems().size() - 1;
        if (lastIndex >= 0) { // Esegue: if (lastIndex >= 0) {
            listView.scrollTo(lastIndex); // Esegue: listView.scrollTo(lastIndex);
        } // Esegue: }
    } // Esegue: }
} // Esegue: }
