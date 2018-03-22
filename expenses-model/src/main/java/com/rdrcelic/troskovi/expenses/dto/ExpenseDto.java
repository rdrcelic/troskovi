package com.rdrcelic.troskovi.expenses.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExpenseDto {
    private String description;
    @JsonSerialize(using = ToStringSerializer.class) // keep decimal number as is without rounding
    private BigDecimal amount;
    private Boolean active = true;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss.SSSXXX")
    private Date timeCreated;

    public ExpenseDto() {}
    public ExpenseDto(String description, BigDecimal amount) {
        this.description = description;
        this.amount = amount;
        this.active = true;
    }

    @JsonCreator
    public ExpenseDto(@JsonProperty(value = "description") String description,
                      @JsonProperty(value = "amount") BigDecimal amount,
                      @JsonProperty(value = "createdAt") Date createdAt,
                      @JsonProperty("active") boolean active) {
        this.active = active;
        this.timeCreated = createdAt;
        this.description = description;
        this.amount = amount;
    }

    @JsonProperty("createdAt")
    public Date getTimeCreated() {
        return timeCreated;
    }

}
