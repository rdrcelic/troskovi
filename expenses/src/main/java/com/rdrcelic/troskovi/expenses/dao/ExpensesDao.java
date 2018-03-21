package com.rdrcelic.troskovi.expenses.dao;

import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import com.rdrcelic.troskovi.expenses.exceptions.ExpenseConflictExeption;
import com.rdrcelic.troskovi.expenses.exceptions.NoSuchExpense;
import com.rdrcelic.troskovi.expenses.repository.ExpensesRepository;
import com.rdrcelic.troskovi.expenses.utility.ModelMapperFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This is data access object to manage Expense entities.
 * This is Expense persistency layer interface and only through this object it is possible to reach data from data store.
 */
@Component
public class ExpensesDao {

    private final ExpensesRepository expensesRepository;
    private final ModelMapper modelMapper = ModelMapperFactory.createModelMapperSkippingNullProperties();

    public ExpensesDao(ExpensesRepository expensesRepository) {
        this.expensesRepository = expensesRepository;
    }

    public List<ExpenseDto> getAllExpenses() {
        Optional<List<ExpenseEntity>> optionalList = Optional.of((List<ExpenseEntity>) expensesRepository.findAll());
        List<ExpenseEntity> expenseEntityList = optionalList.orElse(new ArrayList<>());
        return expenseEntityList
                .stream()
                .map(expenseEntity -> modelMapper.map(expenseEntity, ExpenseDto.class))
                .collect(Collectors.toList());
    }

    public ExpenseEntity createExpense(ExpenseDto newExpenseDto) throws ExpenseConflictExeption {
        //TODO: validate newExpenseDto
        ExpenseEntity newExpenseEntity = modelMapper.map(newExpenseDto, ExpenseEntity.class);
        return expensesRepository.save(newExpenseEntity);
    }

    public ExpenseEntity getExpense(long id) throws NoSuchExpense {
        Optional<ExpenseEntity> expenseEntity = expensesRepository.findById(id);
        return expenseEntity.orElseThrow(() -> new NoSuchExpense(id));
    }

    public void patchExpense(long id, ExpenseDto expenseDto) throws InvalidParameterException {
        ExpenseEntity expenseToChange = this.getExpense(id);
        // validate expenseEssentialsOnly and determine which parameter is invalid - validatation throws InvalidParameterException exception
        modelMapper.map(expenseDto, expenseToChange);
        expensesRepository.save(expenseToChange);
    }
}
