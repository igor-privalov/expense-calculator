package com.innowise.expensio.dto;

import java.math.BigDecimal;

public record ExpenseDto(
        String id,
        String category,
        BigDecimal amount
) {
}
