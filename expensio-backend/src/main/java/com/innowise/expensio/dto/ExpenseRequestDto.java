package com.innowise.expensio.dto;

import java.math.BigDecimal;

public record ExpenseRequestDto(
        String category,
        BigDecimal amount) {
}
