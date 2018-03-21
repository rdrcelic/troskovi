package com.rdrcelic.troskovi.expenses.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseDto {
    private String description;
    @JsonSerialize(using = ToStringSerializer.class) // keep decimal number as is without rounding
    private BigDecimal amount;
    private Boolean active = true;

    public ExpenseDto() {}
    public ExpenseDto(String description, BigDecimal amount) {
        this.description = description;
        this.amount = amount;
        this.active = true;
    }
}
