package com.rdrcelic.troskovi.controllers;

import com.rdrcelic.troskovi.dao.ExpensesDao;
import com.rdrcelic.troskovi.model.TroskoviResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

@RestController
public class ExpensesController {

    private ExpensesDao expensesDao;

    public ExpensesController(ExpensesDao expensesDao) {
        this.expensesDao = expensesDao;
    }

    @GetMapping(value = "/expenses", produces = "application/json; charset=UTF-8")
    public TroskoviResult getAllExpenses() {
        TroskoviResult troskoviResult = TroskoviResult.createResult("expenses", expensesDao.getAllExpenses());
        return troskoviResult;
    }
}
