package com.innowise.expensio.dto;

import java.math.BigDecimal;
import java.util.List;

public record ExpenseStatsDto(
        BigDecimal total,
        BigDecimal averageDaily,
        List<ExpenseDto> topExpenses
) {
}








