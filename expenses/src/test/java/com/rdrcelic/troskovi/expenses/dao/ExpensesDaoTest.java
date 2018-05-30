package com.rdrcelic.troskovi.expenses.dao;

import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import com.rdrcelic.troskovi.expenses.exceptions.NoSuchExpense;
import com.rdrcelic.troskovi.expenses.extensions.MockitoExtension;
import com.rdrcelic.troskovi.expenses.repository.ExpensesRepository;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * This is unit test for ExpenseDao
 */
@RunWith(JUnitPlatform.class)
@ExtendWith(MockitoExtension.class)
public class ExpensesDaoTest {

    private EnhancedRandom enhancedRandom;

    @Mock
    private ExpensesRepository expensesRepository;

    private ExpensesDao expensesDao;

    @BeforeEach
    public void setup() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
        expensesDao = new ExpensesDao(expensesRepository);
    }

    @Test
    public void getAllExpenses() {
        // given
        when(expensesRepository.findAll()).thenReturn(Arrays.asList(enhancedRandom.nextObject(ExpenseEntity.class)));
        // when
        List<ExpenseDto> expenseDtoList = expensesDao.getAllExpenses();
        // then
        assertThat(expenseDtoList).hasSize(1);
    }

    @Test
    public void createExpense() {
        // given
        ExpenseDto newExpenseDto = enhancedRandom.nextObject(ExpenseDto.class, "active");
        ModelMapper modelMapper = new ModelMapper();
        ExpenseEntity newExpenseEntity = modelMapper.map(newExpenseDto, ExpenseEntity.class);
        when(expensesRepository.save(any(ExpenseEntity.class))).thenReturn(newExpenseEntity);
        // when
        ExpenseEntity createdExpenseEntity = expensesDao.createExpense(newExpenseDto);
        // then
        assertAll(
                () -> assertThat(createdExpenseEntity.getActive()).isTrue(),
                () -> assertThat(createdExpenseEntity).isEqualTo(newExpenseEntity));
    }

    @Test
    public void getExpenseOK() {
        // given
        when(expensesRepository.findById(anyLong())).thenReturn(Optional.of(enhancedRandom.nextObject(ExpenseEntity.class)));
        // when
        ExpenseEntity expenseEntity = expensesDao.getExpense(101);
        // then
        assertThat(expenseEntity).isNotNull();
    }

    @Test
    public void getExpenseNOK() {
        // given
        when(expensesRepository.findById(anyLong())).thenReturn(Optional.empty());
        // when
        Throwable thrown = catchThrowable(() -> expensesDao.getExpense(101));
        // then
        assertThat(thrown)
                .isInstanceOf(NoSuchExpense.class)
                .hasMessage("No such expense");
    }

    @Test
    public void patchExpense() {
        // given
        ExpenseDto changeInExpense = new ExpenseDto();
        changeInExpense.setAmount(BigDecimal.ONE); // we want to change only amount!!!
        ExpenseEntity expenseChanged = enhancedRandom.nextObject(ExpenseEntity.class);
        expenseChanged.setActive(true);
        expenseChanged.setDescription("foo");
        when(expensesRepository.findById(eq(101L))).thenReturn(Optional.of(expenseChanged));
        // when
        expensesDao.patchExpense(101L, changeInExpense);
        // then
        verify(expensesRepository, times(1)).save(any(ExpenseEntity.class));
        assertAll(
                // make sure only amount has been changed
                () -> assertThat(expenseChanged.getAmount()).isEqualTo(BigDecimal.ONE),
                // and all other fields stayed the same
                () -> assertThat(expenseChanged.getActive()).isTrue(),
                () -> assertThat(expenseChanged.getDescription()).isEqualTo("foo"));
    }
}