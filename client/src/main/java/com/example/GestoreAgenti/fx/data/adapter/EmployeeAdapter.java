package com.example.GestoreAgenti.fx.data.adapter;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

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
        String fullName = Arrays.stream(new String[] {dto.firstName(), dto.lastName()})
                .filter(part -> part != null && !part.isBlank())
                .collect(Collectors.joining(" "));
        return new Employee(dto.id(), fullName, dto.role(), dto.team(), dto.email());
    }

    public EmployeeDto toDto(Employee employee) {
        if (employee == null) {
            return null;
        }
        String[] parts = Objects.toString(employee.fullName(), "").trim().split(" ", 2);
        String firstName = parts.length > 0 ? parts[0] : "";
        String lastName = parts.length > 1 ? parts[1] : "";
        return new EmployeeDto(employee.id(), firstName, lastName, employee.role(), employee.teamName(), employee.email());
    }
}
