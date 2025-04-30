package com.innowise.expensio.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "expenses")
public class Expense {

    @Id
    private String id;

    private String category;

    private BigDecimal amount;

}
