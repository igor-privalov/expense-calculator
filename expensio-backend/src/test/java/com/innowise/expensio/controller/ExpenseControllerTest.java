package com.innowise.expensio.controller;

import com.innowise.expensio.dto.ExpenseDto;
import com.innowise.expensio.dto.ExpenseRequestDto;
import com.innowise.expensio.dto.ExpenseStatsDto;
import com.innowise.expensio.service.ExpenseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(ExpenseController.class)
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ExpenseService expenseService;

    @Test
    void saveExpense_ShouldReturnCreatedExpense() throws Exception {
        // Arrange
        ExpenseRequestDto requestDto = new ExpenseRequestDto("Test Category", new BigDecimal("100.00"));
        ExpenseDto responseDto = new ExpenseDto("1", "Test Category", new BigDecimal("100.00"));

        when(expenseService.saveExpense(any(ExpenseRequestDto.class))).thenReturn(responseDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("1")))
                .andExpect(jsonPath("$.category", is("Test Category")))
                .andExpect(jsonPath("$.amount", is(100.00)));
    }

    @Test
    void saveExpense_ShouldReturnBadRequest_WhenCategoryIsBlank() throws Exception {
        // Arrange
        ExpenseRequestDto invalidRequest = new ExpenseRequestDto("", new BigDecimal("100.00"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.category", is("Category cannot be blank")));
    }

    @Test
    void saveExpense_ShouldReturnBadRequest_WhenAmountIsNull() throws Exception {
        // Arrange
        String invalidRequest = "{\"category\":\"Test Category\",\"amount\":null}";

        // Act & Assert
        mockMvc.perform(post("/api/v1/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount", is("Amount cannot be null")));
    }

    @Test
    void saveExpense_ShouldReturnBadRequest_WhenAmountIsNegative() throws Exception {
        // Arrange
        ExpenseRequestDto invalidRequest = new ExpenseRequestDto("Test Category", new BigDecimal("-100.00"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.amount", is("Amount must be greater than 0")));
    }

    @Test
    void findAllExpenses_ShouldReturnPageOfExpenses() throws Exception {
        // Arrange
        ExpenseDto expenseDto = new ExpenseDto("1", "Test Category", new BigDecimal("100.00"));
        List<ExpenseDto> expenses = Arrays.asList(expenseDto);
        Page<ExpenseDto> page = new PageImpl<>(expenses, PageRequest.of(0, 10), 1);

        when(expenseService.findAllExpenses(any(Pageable.class))).thenReturn(page);

        // Act & Assert
        mockMvc.perform(get("/api/v1/expenses?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is("1")))
                .andExpect(jsonPath("$.content[0].category", is("Test Category")))
                .andExpect(jsonPath("$.content[0].amount", is(100.00)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void getExpenseStats_ShouldReturnStats() throws Exception {
        // Arrange
        ExpenseDto topExpense1 = new ExpenseDto("1", "Category1", new BigDecimal("200.00"));
        ExpenseDto topExpense2 = new ExpenseDto("2", "Category2", new BigDecimal("100.00"));
        List<ExpenseDto> topExpenses = Arrays.asList(topExpense1, topExpense2);
        ExpenseStatsDto statsDto = new ExpenseStatsDto(
                new BigDecimal("350.00"),
                new BigDecimal("11.67"),
                topExpenses
        );

        when(expenseService.getExpenseStats(anyInt())).thenReturn(statsDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/expenses/stats?topCount=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", is(350.00)))
                .andExpect(jsonPath("$.averageDaily", is(11.67)))
                .andExpect(jsonPath("$.topExpenses", hasSize(2)))
                .andExpect(jsonPath("$.topExpenses[0].amount", is(200.00)))
                .andExpect(jsonPath("$.topExpenses[1].amount", is(100.00)));
    }

    @Test
    void findAllExpenses_ShouldReturnBadRequest_WhenInvalidPagination() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/expenses?page=-1&size=0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExpenseStats_ShouldReturnBadRequest_WhenInvalidTopCount() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/expenses/stats?topCount=-1"))
                .andExpect(status().isBadRequest());
    }
} 