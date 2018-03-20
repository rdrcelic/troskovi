package com.rdrcelic.troskovi.expenses.controllers;

import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {

    private final ExpensesDao expensesDao;

    public ExpensesController(ExpensesDao expensesDao) {
        this.expensesDao = expensesDao;
    }

    @GetMapping(produces = "application/json; charset=UTF-8")
    public TroskoviResult getAllExpenses() {
        return TroskoviResult.createResult("expenses", expensesDao.getAllExpenses());
    }

    @PostMapping(consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> createExpense(@RequestBody ExpenseDto newExpenseDto) {
        ExpenseEntity newExpenseEntity = expensesDao.createExpense(newExpenseDto);

        ModelMapper modelMapper = new ModelMapper();
        ExpenseDto returnExpenseDto = modelMapper.map(newExpenseEntity, ExpenseDto.class);
        TroskoviResult troskoviResult = TroskoviResult.createResult("newExpense", returnExpenseDto);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON_UTF8).body(troskoviResult);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json; charset=UTF-8")
    public ResponseEntity<?> patchExpenseEssentials(@PathVariable Long id, @RequestBody ExpenseDto expenseDto) {
        expensesDao.patchExpense(id, expenseDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
