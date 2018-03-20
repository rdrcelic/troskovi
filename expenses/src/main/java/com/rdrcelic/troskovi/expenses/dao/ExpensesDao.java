package com.rdrcelic.troskovi.expenses.dao;

import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import com.rdrcelic.troskovi.expenses.exceptions.ExpenseConflictExeption;
import com.rdrcelic.troskovi.expenses.exceptions.NoSuchExpense;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This is data access object to manage Expense entities.
 * This is Expense persistency layer interface and only through this object it is possible to reach data from data store.
 */
@Component
public class ExpensesDao {

    public List<ExpenseDto> getAllExpenses() {
        return new ArrayList<>();
    }

    public ExpenseEntity createExpense(ExpenseDto newExpenseDto) throws ExpenseConflictExeption {
        return new ExpenseEntity();
    }

    public ExpenseEntity getExpense(long id) throws NoSuchExpense {
        return new ExpenseEntity();
    }

    public void patchExpense(long id, ExpenseDto expenseDto) throws InvalidParameterException {
        ExpenseEntity expenseToChange = this.getExpense(id);

        // validate expenseEssentialsOnly and determine which parameter is invalid - validatation throws InvalidParameterException exception

    }
}
