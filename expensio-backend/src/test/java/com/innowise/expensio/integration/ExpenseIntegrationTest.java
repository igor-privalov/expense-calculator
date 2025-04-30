package com.innowise.expensio.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.innowise.expensio.dto.ExpenseDto;
import com.innowise.expensio.dto.ExpenseRequestDto;
import com.innowise.expensio.dto.ExpenseStatsDto;
import com.innowise.expensio.model.Expense;
import com.innowise.expensio.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExpenseIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:8");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ExpenseRepository expenseRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/expenses";
        expenseRepository.deleteAll();
    }

    @Test
    void shouldSaveAndRetrieveExpense() throws Exception {
        // Arrange
        String category = "Food";
        BigDecimal amount = new BigDecimal("100.00");
        ExpenseRequestDto requestDto = new ExpenseRequestDto(category, amount);

        // Act - Save expense
        ResponseEntity<ExpenseDto> saveResponse = restTemplate.postForEntity(
                baseUrl,
                requestDto,
                ExpenseDto.class
        );

        // Assert - Save response
        assertThat(saveResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(saveResponse.getBody()).isNotNull();
        assertThat(saveResponse.getBody().category()).isEqualTo(category);
        assertThat(saveResponse.getBody().amount()).isEqualByComparingTo(amount);

        // Act - Retrieve expenses
        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                baseUrl + "?page=0&size=10",
                String.class
        );

        // Assert - Get response
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        
        Map<String, Object> responseMap = objectMapper.readValue(getResponse.getBody(), new TypeReference<Map<String, Object>>() {});
        assertThat(responseMap.get("totalElements")).isEqualTo(1);
        
        List<Map<String, Object>> content = (List<Map<String, Object>>) responseMap.get("content");
        assertThat(content).hasSize(1);
        assertThat(content.get(0).get("category")).isEqualTo(category);
        assertThat(new BigDecimal(content.get(0).get("amount").toString())).isEqualByComparingTo(amount);
    }

    @Test
    void shouldCalculateExpenseStats() throws Exception {
        // Arrange
        List<Expense> expenses = List.of(
                createExpense("Food", "100.00"),
                createExpense("Transport", "50.00"),
                createExpense("Entertainment", "200.00")
        );
        expenseRepository.saveAll(expenses);

        // Act
        ResponseEntity<ExpenseStatsDto> response = restTemplate.getForEntity(
                baseUrl + "/stats?topCount=2",
                ExpenseStatsDto.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().total()).isEqualByComparingTo("350.00");
        assertThat(response.getBody().averageDaily()).isEqualByComparingTo("11.67");
        assertThat(response.getBody().topExpenses()).hasSize(2);
        assertThat(response.getBody().topExpenses().get(0).amount()).isEqualByComparingTo("200.00");
        assertThat(response.getBody().topExpenses().get(1).amount()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldReturnBadRequestForInvalidInput() {
        // Arrange
        ExpenseRequestDto invalidRequest = new ExpenseRequestDto("", new BigDecimal("-100.00"));

        // Act
        ResponseEntity<Object> response = restTemplate.postForEntity(
                baseUrl,
                invalidRequest,
                Object.class
        );

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private Expense createExpense(String category, String amount) {
        Expense expense = new Expense();
        expense.setCategory(category);
        expense.setAmount(new BigDecimal(amount));
        return expense;
    }
} 