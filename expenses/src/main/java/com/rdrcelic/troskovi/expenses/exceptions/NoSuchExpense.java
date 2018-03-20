package com.rdrcelic.troskovi.expenses.exceptions;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="This id doesn't match any known expense")
public class NoSuchExpense extends RuntimeException {
    @Getter
    private final long expenseId;

    public NoSuchExpense(long id, String message) {
        super(message);
        this.expenseId = id;
    }
}
