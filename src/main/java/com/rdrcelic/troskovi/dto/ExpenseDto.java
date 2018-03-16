package com.rdrcelic.troskovi.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseDto {
    private String expenseDescription;
    private BigDecimal amount;
}
