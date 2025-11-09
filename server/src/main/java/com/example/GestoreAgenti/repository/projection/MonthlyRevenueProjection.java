package com.example.GestoreAgenti.repository.projection;

import java.math.BigDecimal;

public interface MonthlyRevenueProjection {
    Integer getAnno();

    Integer getMese();

    BigDecimal getTotale();
}
