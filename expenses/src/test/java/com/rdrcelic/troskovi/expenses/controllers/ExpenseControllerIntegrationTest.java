package com.rdrcelic.troskovi.expenses.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExpenseControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ExpensesDao mockExpenseDao; // TODO: when persistency implemented this mock should disappear

    private EnhancedRandom enhancedRandom;
    private ExpenseDto testExpenseDto;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
        testExpenseDto = enhancedRandom.nextObject(ExpenseDto.class);
    }

    @Test
    public void getAllExpenses() throws Exception {
        // given
        when(mockExpenseDao.getAllExpenses()).thenReturn(Arrays.asList(testExpenseDto));
        // when
        ResponseEntity<TroskoviResult> response = restTemplate.getForEntity("/expenses", TroskoviResult.class);
        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().get(0).get("expenses")).isOfAnyClassIn(List.class, ArrayList.class);
        assertThat((List)response.getBody().get(0).get("expenses")).hasSize(1);

        // TODO: extract this part to some utility
        List<ExpenseDto> expenseDtoList = objectMapper.convertValue(
                (List)response.getBody().get(0).get("expenses"),
                new TypeReference<List<ExpenseDto>>() {});


        assertThat(expenseDtoList.get(0).getAmount().setScale(10, BigDecimal.ROUND_DOWN))
                .isEqualTo(testExpenseDto.getAmount().setScale(10, BigDecimal.ROUND_DOWN));
        assertThat(expenseDtoList.get(0).getExpenseDescription()).isEqualTo(testExpenseDto.getExpenseDescription());
    }
}
