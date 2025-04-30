package com.innowise.expensio.controller;

import com.innowise.expensio.dto.ExpenseDto;
import com.innowise.expensio.dto.ExpenseRequestDto;
import com.innowise.expensio.dto.ExpenseStatsDto;
import com.innowise.expensio.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ExpenseDto saveExpense(@Valid @RequestBody ExpenseRequestDto expenseRequestDto) {
        return expenseService.saveExpense(expenseRequestDto);
    }

    @GetMapping
    public Page<ExpenseDto> findAllExpenses(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return expenseService.findAllExpenses(pageable);
    }

    @GetMapping("/stats")
    public ExpenseStatsDto getExpenseStats(@RequestParam int topCount) {
        return expenseService.getExpenseStats(topCount);
    }
}
