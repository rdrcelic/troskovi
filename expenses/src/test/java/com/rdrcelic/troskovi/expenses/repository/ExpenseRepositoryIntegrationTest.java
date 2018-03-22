package com.rdrcelic.troskovi.expenses.repository;

import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.assertj.core.api.Condition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is component integration test for ExpenseRepository
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ExpenseRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ExpensesRepository expensesRepository;

    private EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    private ExpenseEntity randomEntity;

    @After
    public void cleaup() {
        entityManager.clear();
    }

    @Before
    public void setup() {
        randomEntity = enhancedRandom.nextObject(ExpenseEntity.class);
        randomEntity.setId(null); // has to be done to avoid "org.hibernate.PersistentObjectException: detached entity passed to persist"
    }

    @Test
    public void saveExpense() {
        // given
        ExpenseEntity newExpenseEntity = enhancedRandom.nextObject(ExpenseEntity.class);
        newExpenseEntity.setId(-1L);
        // when
        Long newId = expensesRepository.save(newExpenseEntity).getId();
        ExpenseEntity expenseEntitySaved = entityManager.find(ExpenseEntity.class, newId);
                // then
        assertThat(expenseEntitySaved).isNotNull();
        assertThat(expenseEntitySaved.getId()).is(new Condition<Long>(id -> id > 0, "Id has to be greater than 0"));
    }

    @Test
    public void getExpenseByIdOK() {
        // given
        ExpenseEntity expenseEntitySaved = entityManager.persist(randomEntity);
        // when
        ExpenseEntity requestedEntity = expensesRepository.findById(expenseEntitySaved.getId()).orElse(null);
        // then
        assertThat(requestedEntity).isNotNull();
        assertThat(requestedEntity).isEqualTo(expenseEntitySaved);
    }

    @Test
    public void getExpenseByIdNOK() {
        // given
        ExpenseEntity expenseEntitySaved = entityManager.persist(randomEntity);
        // when
        ExpenseEntity requestedExpenseEntity = expensesRepository.findById(expenseEntitySaved.getId() + 1L).orElse(null);
        // then
        assertThat(requestedExpenseEntity).isNull();
    }

    @Test
    public void getAllExpenses() {
        // given
        ExpenseEntity secondRandomExpense = enhancedRandom.nextObject(ExpenseEntity.class);
        secondRandomExpense.setId(null);
        entityManager.persist(randomEntity);
        entityManager.persist(secondRandomExpense);
        // when
        Iterable<ExpenseEntity> allExpenses = expensesRepository.findAll();
        // then
        assertThat(allExpenses).hasSize(2);
    }

    @Test
    public void updateExpense() {
        // given
        ExpenseEntity expenseEntity = entityManager.persist(randomEntity);
        // when
        boolean patchedValue = !expenseEntity.getActive();
        expenseEntity.setActive(patchedValue);
        ExpenseEntity updatedExpenseEntity = expensesRepository.save(expenseEntity);
        // then
        assertThat(updatedExpenseEntity.getActive()).isEqualTo(patchedValue);
    }
}
