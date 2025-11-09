package com.example.GestoreAgenti.controller.dto;

import java.math.BigDecimal;

public record MonthlyRevenueDto(Integer year, Integer month, BigDecimal total) {
}
