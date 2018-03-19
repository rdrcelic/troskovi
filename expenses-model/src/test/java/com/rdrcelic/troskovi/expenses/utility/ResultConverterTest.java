package com.rdrcelic.troskovi.expenses.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.model.TroskoviResult;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ResultConverterTest {

    private EnhancedRandom enhancedRandom;
    private ResultConverter resultConverter;
    private ObjectMapper objectMapper;
    {
        objectMapper = new ObjectMapper();
        resultConverter = new ResultConverter(objectMapper);
    }

    @Before
    public void setup() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    }

    @Test
    public void extractListOfExpenseDtosFromTroskoviResult() {
        // given
        ExpenseDto testExpenseDto = enhancedRandom.nextObject(ExpenseDto.class);
        String valueKey = "expenses";
        TroskoviResult expensesResult = TroskoviResult.createResult(valueKey, Arrays.asList(testExpenseDto));
        List<ExpenseDto> expenseDtos = new ArrayList<>();
        // when
        expenseDtos = resultConverter.convertValue(expensesResult, valueKey, expenseDtos);
        // then
        assertThat(expenseDtos).hasSize(1);
        assertThat(expenseDtos.get(0).getExpenseDescription()).isEqualTo(testExpenseDto.getExpenseDescription());
        assertThat(expenseDtos.get(0).getAmount().setScale(10, BigDecimal.ROUND_DOWN))
                .isEqualTo(testExpenseDto.getAmount().setScale(10, BigDecimal.ROUND_DOWN));
    }
}