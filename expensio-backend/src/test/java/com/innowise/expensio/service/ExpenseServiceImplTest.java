package com.innowise.expensio.service;

import com.innowise.expensio.dto.ExpenseDto;
import com.innowise.expensio.dto.ExpenseRequestDto;
import com.innowise.expensio.dto.ExpenseStatsDto;
import com.innowise.expensio.mapper.ExpenseMapper;
import com.innowise.expensio.model.Expense;
import com.innowise.expensio.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceImplTest {

    @Mock
    private ExpenseRepository repository;

    @Mock
    private ExpenseMapper mapper;

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    private ExpenseRequestDto expenseRequestDto;
    private Expense expense;
    private ExpenseDto expenseDto;

    @BeforeEach
    void setUp() {
        expenseRequestDto = new ExpenseRequestDto(
                "Test Category",
                new BigDecimal("100.00")
        );

        expense = new Expense();
        expense.setId("1");
        expense.setCategory("Test Category");
        expense.setAmount(new BigDecimal("100.00"));

        expenseDto = new ExpenseDto(
                "1",
                "Test Category",
                new BigDecimal("100.00")
        );
    }

    @Test
    void saveExpense_ShouldSaveAndReturnExpenseDto() {
        // Arrange
        when(mapper.toModel(expenseRequestDto)).thenReturn(expense);
        when(repository.save(expense)).thenReturn(expense);
        when(mapper.toDto(expense)).thenReturn(expenseDto);

        // Act
        ExpenseDto result = expenseService.saveExpense(expenseRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(expenseDto, result);
        verify(repository).save(expense);
        verify(mapper).toModel(expenseRequestDto);
        verify(mapper).toDto(expense);
    }

    @Test
    void findAllExpenses_ShouldReturnPageOfExpenseDtos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        List<Expense> expenses = Arrays.asList(expense);
        Page<Expense> expensePage = new PageImpl<>(expenses, pageable, expenses.size());
        
        when(repository.findAll(pageable)).thenReturn(expensePage);
        when(mapper.toDto(expense)).thenReturn(expenseDto);

        // Act
        Page<ExpenseDto> result = expenseService.findAllExpenses(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(expenseDto, result.getContent().get(0));
        verify(repository).findAll(pageable);
        verify(mapper).toDto(expense);
    }

    @Test
    void getExpenseStats_ShouldReturnCorrectStats() {
        // Arrange
        int topCount = 2;
        List<Expense> expenses = Arrays.asList(
                createExpense("1", "100.00"),
                createExpense("2", "200.00"),
                createExpense("3", "50.00")
        );

        when(repository.findAll()).thenReturn(expenses);
        when(mapper.toDto(any(Expense.class))).thenAnswer(invocation -> {
            Expense e = invocation.getArgument(0);
            return new ExpenseDto(e.getId(), e.getCategory(), e.getAmount());
        });

        // Act
        ExpenseStatsDto result = expenseService.getExpenseStats(topCount);

        // Assert
        assertNotNull(result);
        assertEquals(new BigDecimal("350.00"), result.total());
        assertEquals(new BigDecimal("11.67"), result.averageDaily());
        assertEquals(2, result.topExpenses().size());
        assertEquals(new BigDecimal("200.00"), result.topExpenses().get(0).amount());
        assertEquals(new BigDecimal("100.00"), result.topExpenses().get(1).amount());
    }

    private Expense createExpense(String id, String amount) {
        Expense expense = new Expense();
        expense.setId(id);
        expense.setCategory("Test Category");
        expense.setAmount(new BigDecimal(amount));
        return expense;
    }
} 