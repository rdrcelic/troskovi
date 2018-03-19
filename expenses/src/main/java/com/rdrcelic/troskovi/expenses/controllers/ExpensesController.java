package com.rdrcelic.troskovi.expenses.controllers;

import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/expenses")
public class ExpensesController {

    private ExpensesDao expensesDao;

    public ExpensesController(ExpensesDao expensesDao) {
        this.expensesDao = expensesDao;
    }

    @GetMapping(produces = "application/json; charset=UTF-8")
    public TroskoviResult getAllExpenses() {
        TroskoviResult troskoviResult = TroskoviResult.createResult("expenses", expensesDao.getAllExpenses());
        return troskoviResult;
    }
}
