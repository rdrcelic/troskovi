package com.rdrcelic.troskovi.expenses.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
public class ExpenseDto {
    private String description;
    private BigDecimal amount;
    private Boolean active;

    /**
     * do not use default constructor from your code
     */
    @Deprecated
    public ExpenseDto() {}

    public ExpenseDto(String expenseDescription, BigDecimal amount) {
        this.description = expenseDescription;
        this.amount = amount;
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }
}
