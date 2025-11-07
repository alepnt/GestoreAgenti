package com.example.GestoreAgenti.fx.data.adapter;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;
import com.example.GestoreAgenti.fx.model.Employee;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeAdapterTest {

    private final EmployeeAdapter adapter = new EmployeeAdapter();

    @Test
    void toModelReturnsNullWhenDtoIsNull() {
        assertNull(adapter.toModel(null));
    }

    @Test
    void toModelTrimsFieldsAndBuildsFullName() {
        EmployeeDto dto = new EmployeeDto("  ID-1  ", " Mario  ", "  Rossi", "  Manager  ", "  Team Uno ", "  mario@example.com  ");
        Employee model = adapter.toModel(dto);
        assertEquals("ID-1", model.id());
        assertEquals("Mario Rossi", model.fullName());
        assertEquals("Manager", model.role());
        assertEquals("Team Uno", model.teamName());
        assertEquals("mario@example.com", model.email());
    }

    @Test
    void toModelThrowsWhenIdentifierIsMissing() {
        EmployeeDto dto = new EmployeeDto("  ", "Mario", "Rossi", "Manager", "Vendite", "mario@example.com");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> adapter.toModel(dto));
        assertTrue(ex.getMessage().contains("identificativo"));
    }

    @Test
    void toDtoReturnsNullWhenEmployeeIsNull() {
        assertNull(adapter.toDto(null));
    }

    @Test
    void toDtoSplitsFullNameAndTrimsFields() {
        Employee employee = new Employee("  ID-2  ", " Anna  Verdi ", "  Consulente  ", "  Team Ovest  ", "  anna@example.com  ");
        EmployeeDto dto = adapter.toDto(employee);
        assertEquals("ID-2", dto.id());
        assertEquals("Anna", dto.firstName());
        assertEquals("Verdi", dto.lastName());
        assertEquals("Consulente", dto.role());
        assertEquals("Team Ovest", dto.team());
        assertEquals("anna@example.com", dto.email());
    }

    @Test
    void toDtoHandlesSingleWordNames() {
        Employee employee = new Employee("ID-3", "Cher", "Cantante", null, null);
        EmployeeDto dto = adapter.toDto(employee);
        assertEquals("Cher", dto.firstName());
        assertEquals("", dto.lastName());
    }

    @Test
    void toDtoHandlesEmptyFullName() {
        Employee employee = new Employee("ID-4", "   ", null, null, null);
        EmployeeDto dto = adapter.toDto(employee);
        assertEquals("", dto.firstName());
        assertEquals("", dto.lastName());
    }
}
