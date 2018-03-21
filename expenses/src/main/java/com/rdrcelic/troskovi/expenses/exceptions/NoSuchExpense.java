package com.rdrcelic.troskovi.expenses.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason="No such expense")
public class NoSuchExpense extends RuntimeException {
    @Getter
    private final long expenseId;

    public NoSuchExpense(long id) {
        super("No such expense");
        this.expenseId = id;
    }
}
