package com.rdrcelic.troskovi.expenses.dao;

import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ExpensesDao {

    public List<ExpenseDto> getAllExpenses() {
        return new ArrayList<>();
    }
}
