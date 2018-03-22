package com.rdrcelic.troskovi.expenses.jackson;

import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * This is component test to make sure Jackson updates doesn't break JSON format expectations
 */
@RunWith(SpringRunner.class)
@JsonTest
public class JacksonExpenseDtoCompatibilityTest {

    private EnhancedRandom enhancedRandom;

    @Autowired
    private JacksonTester<ExpenseDto> jsonTester;

    @Before
    public void setup() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    }
    @Test
    public void serializeToJson() throws Exception {
        // given
        ExpenseDto dto = new ExpenseDto();
        dto.setAmount(new BigDecimal("10.00"));
        dto.setDescription("demo");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss.SSSXXX");
        Date date = formatter.parse("22-03-2018T15:01:34.446Z");
        dto.setTimeCreated(date);
        ClassPathResource resource = new ClassPathResource("jackson/json/fullExpenseDto.json");
        // then
        assertThat(this.jsonTester.write(dto)).isEqualToJson(resource.getInputStream());
    }

    @Test
    public void deserializeJsonToExpenseDto() throws Exception {
        // given
        ClassPathResource resource = new ClassPathResource("jackson/json/deactivateExpense.json");
        // when
        ExpenseDto expenseDto = this.jsonTester.readObject(resource.getInputStream());
        // then
        assertThat(expenseDto.getActive()).isFalse();
        assertThat(expenseDto.getAmount()).isNull();
        assertThat(expenseDto.getDescription()).isNull();
        assertThat(expenseDto.getTimeCreated()).isNull();
    }
}
