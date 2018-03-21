package com.rdrcelic.troskovi.expenses.controllers;

import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpenseControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExpensesDao expensesDao;

    private ExpenseDto testExpenseDto;

    @Before
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
        assertThat((List)response.getBody().get(0).get("expenses")).hasSize(1);
    }
}
