package com.rdrcelic.troskovi.expenses.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason="This expense conflicts with some other expense already saved")
public class ExpenseConflictExeption extends RuntimeException {

    public ExpenseConflictExeption(String message) {
        super(message);
    }
}
