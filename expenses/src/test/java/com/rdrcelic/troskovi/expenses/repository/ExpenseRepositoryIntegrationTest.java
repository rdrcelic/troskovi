package com.rdrcelic.troskovi.expenses.repository;

import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ExpenseRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExpensesRepository expensesRepository;

    private EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();

    @After
    public void cleaup() {
        expensesRepository.deleteAll();
    }

    @Test
    public void saveExpense() {
        // given
        ExpenseEntity newExpenseEntity = enhancedRandom.nextObject(ExpenseEntity.class);
        newExpenseEntity.setId(-1L);
        // when
        ExpenseEntity expenseEntitySaved = expensesRepository.save(newExpenseEntity);
        // then
        assertThat(expenseEntitySaved).isNotNull();
        assertThat(expenseEntitySaved.getId()).is(new Condition<Long>(id -> id > 0, "Id has to be greater than 0"));
    }

    @Test
    public void getExpenseByIdOK() {
        // given
        ExpenseEntity expenseEntitySaved = expensesRepository.save(enhancedRandom.nextObject(ExpenseEntity.class));
        // when
        ExpenseEntity requestedEntity = expensesRepository.findById(expenseEntitySaved.getId()).orElse(null);
        // then
        assertThat(requestedEntity).isNotNull();
        assertThat(requestedEntity).isEqualTo(expenseEntitySaved);
    }

    @Test
    public void getExpenseByIdNOK() {
        // given
        ExpenseEntity expenseEntitySaved = expensesRepository.save(enhancedRandom.nextObject(ExpenseEntity.class));
        // when
        ExpenseEntity requestedExpenseEntity = expensesRepository.findById(expenseEntitySaved.getId() + 1L).orElse(null);
        // then
        assertThat(requestedExpenseEntity).isNull();
    }

    @Test
    public void getAllExpenses() {
        // given
        expensesRepository.save(enhancedRandom.nextObject(ExpenseEntity.class));
        expensesRepository.save(enhancedRandom.nextObject(ExpenseEntity.class));
        // when
        Iterable<ExpenseEntity> allExpenses = expensesRepository.findAll();
        // then
        assertThat(allExpenses).hasSize(2);
    }

    @Test
    public void updateExpense() {
        // given
        ExpenseEntity expenseEntity = expensesRepository.save(enhancedRandom.nextObject(ExpenseEntity.class));
        // when
        boolean patchedValue = !expenseEntity.getActive();
        expenseEntity.setActive(patchedValue);
        ExpenseEntity updatedExpenseEntity = expensesRepository.save(expenseEntity);
        // then
        assertThat(updatedExpenseEntity.getActive()).isEqualTo(patchedValue);
    }

}
