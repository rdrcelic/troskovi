package com.rdrcelic.troskovi.expenses.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseDto {
    private String description;
    private BigDecimal amount;
    private Boolean active = true;
}
