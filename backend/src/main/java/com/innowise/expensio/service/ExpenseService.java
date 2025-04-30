package com.innowise.expensio.service;

import com.innowise.expensio.dto.ExpenseDto;
import com.innowise.expensio.dto.ExpenseRequestDto;
import com.innowise.expensio.dto.ExpenseStatsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExpenseService {

    ExpenseDto saveExpense(ExpenseRequestDto expenseRequestDto);

    Page<ExpenseDto> findAllExpenses(Pageable pageable);

    ExpenseStatsDto getExpenseStats(int topCount);
}
