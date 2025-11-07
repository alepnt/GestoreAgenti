package com.example.GestoreAgenti.fx.data.adapter; // Esegue: package com.example.GestoreAgenti.fx.data.adapter;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto; // Esegue: import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;
import com.example.GestoreAgenti.fx.model.Employee; // Esegue: import com.example.GestoreAgenti.fx.model.Employee;
import org.junit.jupiter.api.Test; // Esegue: import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*; // Esegue: import static org.junit.jupiter.api.Assertions.*;

class EmployeeAdapterTest { // Esegue: class EmployeeAdapterTest {

    private final EmployeeAdapter adapter = new EmployeeAdapter(); // Esegue: private final EmployeeAdapter adapter = new EmployeeAdapter();

    @Test // Esegue: @Test
    void toModelReturnsNullWhenDtoIsNull() { // Esegue: void toModelReturnsNullWhenDtoIsNull() {
        assertNull(adapter.toModel(null)); // Esegue: assertNull(adapter.toModel(null));
    } // Esegue: }

    @Test // Esegue: @Test
    void toModelTrimsFieldsAndBuildsFullName() { // Esegue: void toModelTrimsFieldsAndBuildsFullName() {
        EmployeeDto dto = new EmployeeDto("  ID-1  ", " Mario  ", "  Rossi", "  Manager  ", "  Team Uno ", "  mario@example.com  "); // Esegue: EmployeeDto dto = new EmployeeDto("  ID-1  ", " Mario  ", "  Rossi", "  Manager  ", "  Team Uno ", "  mario@example.com  ");
        Employee model = adapter.toModel(dto); // Esegue: Employee model = adapter.toModel(dto);
        assertEquals("ID-1", model.id()); // Esegue: assertEquals("ID-1", model.id());
        assertEquals("Mario Rossi", model.fullName()); // Esegue: assertEquals("Mario Rossi", model.fullName());
        assertEquals("Manager", model.role()); // Esegue: assertEquals("Manager", model.role());
        assertEquals("Team Uno", model.teamName()); // Esegue: assertEquals("Team Uno", model.teamName());
        assertEquals("mario@example.com", model.email()); // Esegue: assertEquals("mario@example.com", model.email());
    } // Esegue: }

    @Test // Esegue: @Test
    void toModelThrowsWhenIdentifierIsMissing() { // Esegue: void toModelThrowsWhenIdentifierIsMissing() {
        EmployeeDto dto = new EmployeeDto("  ", "Mario", "Rossi", "Manager", "Vendite", "mario@example.com"); // Esegue: EmployeeDto dto = new EmployeeDto("  ", "Mario", "Rossi", "Manager", "Vendite", "mario@example.com");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.toModel(dto)); // Esegue: IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.toModel(dto));
        assertTrue(ex.getMessage().contains("identificativo")); // Esegue: assertTrue(ex.getMessage().contains("identificativo"));
    } // Esegue: }

    @Test // Esegue: @Test
    void toDtoReturnsNullWhenEmployeeIsNull() { // Esegue: void toDtoReturnsNullWhenEmployeeIsNull() {
        assertNull(adapter.toDto(null)); // Esegue: assertNull(adapter.toDto(null));
    } // Esegue: }

    @Test // Esegue: @Test
    void toDtoSplitsFullNameAndTrimsFields() { // Esegue: void toDtoSplitsFullNameAndTrimsFields() {
        Employee employee = new Employee("  ID-2  ", " Anna  Verdi ", "  Consulente  ", "  Team Ovest  ", "  anna@example.com  "); // Esegue: Employee employee = new Employee("  ID-2  ", " Anna  Verdi ", "  Consulente  ", "  Team Ovest  ", "  anna@example.com  ");
        EmployeeDto dto = adapter.toDto(employee); // Esegue: EmployeeDto dto = adapter.toDto(employee);
        assertEquals("ID-2", dto.id()); // Esegue: assertEquals("ID-2", dto.id());
        assertEquals("Anna", dto.firstName()); // Esegue: assertEquals("Anna", dto.firstName());
        assertEquals("Verdi", dto.lastName()); // Esegue: assertEquals("Verdi", dto.lastName());
        assertEquals("Consulente", dto.role()); // Esegue: assertEquals("Consulente", dto.role());
        assertEquals("Team Ovest", dto.team()); // Esegue: assertEquals("Team Ovest", dto.team());
        assertEquals("anna@example.com", dto.email()); // Esegue: assertEquals("anna@example.com", dto.email());
    } // Esegue: }

    @Test // Esegue: @Test
    void toDtoHandlesSingleWordNames() { // Esegue: void toDtoHandlesSingleWordNames() {
        Employee employee = new Employee("ID-3", "Cher", "Cantante", null, null); // Esegue: Employee employee = new Employee("ID-3", "Cher", "Cantante", null, null);
        EmployeeDto dto = adapter.toDto(employee); // Esegue: EmployeeDto dto = adapter.toDto(employee);
        assertEquals("Cher", dto.firstName()); // Esegue: assertEquals("Cher", dto.firstName());
        assertEquals("", dto.lastName()); // Esegue: assertEquals("", dto.lastName());
    } // Esegue: }

    @Test // Esegue: @Test
    void toDtoHandlesEmptyFullName() { // Esegue: void toDtoHandlesEmptyFullName() {
        Employee employee = new Employee("ID-4", "   ", null, null, null); // Esegue: Employee employee = new Employee("ID-4", "   ", null, null, null);
        EmployeeDto dto = adapter.toDto(employee); // Esegue: EmployeeDto dto = adapter.toDto(employee);
        assertEquals("", dto.firstName()); // Esegue: assertEquals("", dto.firstName());
        assertEquals("", dto.lastName()); // Esegue: assertEquals("", dto.lastName());
    } // Esegue: }
} // Esegue: }
