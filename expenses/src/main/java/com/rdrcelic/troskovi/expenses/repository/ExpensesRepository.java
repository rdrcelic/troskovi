package com.rdrcelic.troskovi.expenses.repository;

import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepository extends CrudRepository<ExpenseEntity, Long> {
}
