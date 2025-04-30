package com.innowise.expensio.service;

import com.innowise.expensio.dto.ExpenseDto;
import com.innowise.expensio.dto.ExpenseRequestDto;
import com.innowise.expensio.dto.ExpenseStatsDto;
import com.innowise.expensio.mapper.ExpenseMapper;
import com.innowise.expensio.model.Expense;
import com.innowise.expensio.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository repository;
    private final ExpenseMapper mapper;


    @Override
    public ExpenseDto saveExpense(ExpenseRequestDto expenseRequestDto) {
        Expense expense = mapper.toModel(expenseRequestDto);
        repository.save(expense);

        return mapper.toDto(expense);
    }

    @Override
    public Page<ExpenseDto> findAllExpenses(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    @Override
    public ExpenseStatsDto getExpenseStats(int topCount) {
        List<Expense> expenses = repository.findAll();
        BigDecimal totalAmount = expenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageDailyExpense = totalAmount.divide(BigDecimal.valueOf(30), RoundingMode.HALF_UP);

        List<ExpenseDto> topExpenses = getTopExpense(topCount, expenses);

        return new ExpenseStatsDto(totalAmount, averageDailyExpense, topExpenses);
    }

    private List<ExpenseDto> getTopExpense(int topCount, List<Expense> expenses) {
        return expenses.stream()
                .sorted(Comparator.comparing(Expense::getAmount).reversed())
                .limit(topCount)
                .map(mapper::toDto)
                .toList();
    }
}