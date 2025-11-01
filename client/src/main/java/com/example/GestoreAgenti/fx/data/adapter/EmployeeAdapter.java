package com.example.GestoreAgenti.fx.data.adapter;

import com.example.GestoreAgenti.fx.data.dto.EmployeeDto;
import com.example.GestoreAgenti.fx.model.Employee;

/**
 * Adapter che converte tra il DTO REST e il modello utilizzato dalla GUI.
 */
public class EmployeeAdapter {

    public Employee toModel(EmployeeDto dto) {
        if (dto == null) {
            return null;
        }
        String id = trimToNull(dto.id());
        if (id == null) {
            throw new IllegalArgumentException("L'identificativo del dipendente non può essere vuoto");
        }
        String fullName = buildFullName(trimToNull(dto.firstName()), trimToNull(dto.lastName()));
        return new Employee(id,
                fullName,
                trimToNull(dto.role()),
                trimToNull(dto.team()),
                trimToNull(dto.email()));
    }

    public EmployeeDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        String id = safeTrim(employee.id());
        NameParts parts = splitFullName(employee.fullName());
        return new EmployeeDto(id,
                parts.firstName(),
                parts.lastName(),
                safeTrim(employee.role()),
                safeTrim(employee.teamName()),
                safeTrim(employee.email()));
    }

    private String buildFullName(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            return "";
        }
        if (firstName == null) {
            return lastName;
        }
        if (lastName == null) {
            return firstName;
        }
        return firstName + " " + lastName;
    }

    private NameParts splitFullName(String fullName) {
        if (fullName == null) {
            return new NameParts("", "");
        }
        String normalized = fullName.trim();
        if (normalized.isEmpty()) {
            return new NameParts("", "");
        }
        int separatorIndex = normalized.indexOf(' ');
        if (separatorIndex < 0) {
            return new NameParts(normalized, "");
        }
        String first = normalized.substring(0, separatorIndex);
        String last = normalized.substring(separatorIndex + 1).stripLeading();
        return new NameParts(first, last);
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String safeTrim(String value) {
        return value == null ? null : value.trim();
    }

    private record NameParts(String firstName, String lastName) {
    }
}
