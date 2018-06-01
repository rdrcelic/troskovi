package com.rdrcelic.troskovi.expenses.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;
import com.rdrcelic.troskovi.expenses.utility.ResultConverter;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ITExpenseController {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExpensesDao expensesDao;

    private ResultConverter resultConverter;
    {
        resultConverter = new ResultConverter(new ObjectMapper());
    }
    private ExpenseDto testExpenseDto;

    @BeforeEach
    public void setup() {
        EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
        testExpenseDto = enhancedRandom.nextObject(ExpenseDto.class);
    }

    @Test
    public void getAllExpenses() throws Exception {
        // given
        expensesDao.createExpense(testExpenseDto);
        // when
        ResponseEntity<TroskoviResult> response = restTemplate.getForEntity("/expenses", TroskoviResult.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).get("expenses")).isOfAnyClassIn(List.class, ArrayList.class);
        assertThat((List)response.getBody().get(0).get("expenses")).isNotEmpty();
    }

    @Test
    public void createExpense() throws Exception {
        // given
        ExpenseDto newExpense = new ExpenseDto("Hrana", new BigDecimal("7.40"));
        // when
        ResponseEntity<TroskoviResult> response = restTemplate.postForEntity("/expenses", newExpense, TroskoviResult.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().get(0).get("newExpense")).isInstanceOf(LinkedHashMap.class);
        ExpenseDto expenseDto = resultConverter.convertToExpenseDto(response.getBody(), "newExpense");
        assertThat(expenseDto.getDescription()).isEqualTo("Hrana");
        assertThat(expenseDto.getAmount()).isEqualTo(new BigDecimal("7.40"));
    }

    @Test
    public void deactivateExpense() throws Exception {
        // given
        ExpenseEntity sampleExpense = expensesDao.createExpense(testExpenseDto);
        ExpenseDto expenseChangePayload = new ExpenseDto();
        expenseChangePayload.setActive(false);
        String pathToExpenseResource = String.format("/expenses/%d", sampleExpense.getId());

        // when
        ResponseEntity<?> response = restTemplate.exchange(pathToExpenseResource, HttpMethod.PATCH, new HttpEntity<ExpenseDto>(expenseChangePayload), ResponseEntity.class);
        sampleExpense = expensesDao.getExpense(sampleExpense.getId());
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(sampleExpense.getActive()).isFalse();
    }
}
