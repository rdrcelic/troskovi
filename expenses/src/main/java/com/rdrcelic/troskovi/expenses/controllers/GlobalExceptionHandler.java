package com.rdrcelic.troskovi.expenses.controllers;

import com.rdrcelic.troskovi.common.error.TroskoviError;
import com.rdrcelic.troskovi.expenses.exceptions.ExpenseConflictExeption;
import com.rdrcelic.troskovi.expenses.exceptions.NoSuchExpense;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.MessageFormat;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ExpenseConflictExeption.class)
    public ResponseEntity<TroskoviResult> handleConflict(ExpenseConflictExeption ex) {
        TroskoviResult troskoviResult = TroskoviResult.createResult(
                "error",
                TroskoviError.builder().cause("Not Allowed").message("Expense already exists").build());
        return ResponseEntity.status(HttpStatus.CONFLICT).contentType(MediaType.APPLICATION_JSON_UTF8).body(troskoviResult);
    }

    @ExceptionHandler(NoSuchExpense.class)
    public ResponseEntity<TroskoviResult> handleMissingExpense(NoSuchExpense ex) {
        TroskoviResult troskoviResult = TroskoviResult.createResult(
                "error",
                TroskoviError.builder().cause("Not Found").message(
                        MessageFormat.format("Expense with given id: {0} does not exists", ex.getExpenseId())).build());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON_UTF8).body(troskoviResult);
    }
}
