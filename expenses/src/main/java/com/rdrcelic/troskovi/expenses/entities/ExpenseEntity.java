package com.rdrcelic.troskovi.expenses.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseEntity {
    private Long id;
    private String description;
    private BigDecimal amount;
    private Boolean active;

    @Deprecated
    public ExpenseEntity() {}

    public ExpenseEntity(String description, BigDecimal amount) {
        this.description = description;
        this.amount = amount;
    }
}
