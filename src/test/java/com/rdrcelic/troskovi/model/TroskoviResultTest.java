package com.rdrcelic.troskovi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.rdrcelic.troskovi.dto.ExpenseDto;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/***
 * Test makes sure that various use cases on TroskoviResult are going to have expected JSON structure.
 * [{"key":[{"field":"value"}, {"field":"value"}]}]
 */
public class TroskoviResultTest {

    private ObjectMapper objectMapper;
    {
        objectMapper = new ObjectMapper();
    }

    private EnhancedRandom enhancedRandom;

    @Before
    public void setup() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    }

    @Test
    public void createTroskoviResultOK() throws Exception {
        // given
        TroskoviResult result = TroskoviResult.createResult("array", Arrays.asList("Foo", "Bar"));
        // when
        String jsonResult = objectMapper.writeValueAsString(result);
        // then
        DocumentContext jsonContext = JsonPath.parse(jsonResult);
        assertThat(jsonContext).isNotNull();
        assertThat((List)jsonContext.read("$[0].array")).isNotEmpty();
        assertThat((String)jsonContext.read("$[0].array[0]")).isEqualTo("Foo");
        assertThat((String)jsonContext.read("$[0].array[1]")).isEqualTo("Bar");
    }

    @Test
    public void createTroskoviResultOnExpenseDtoOK() throws Exception {
        // given
        TroskoviResult result = TroskoviResult.createResult("expenses", Arrays.asList(enhancedRandom.nextObject(ExpenseDto.class)));
        // when
        String jsonResult = objectMapper.writeValueAsString(result);
        // then
        DocumentContext jsonContext = JsonPath.parse(jsonResult);
        assertThat(jsonContext).isNotNull();
        assertThat((List)jsonContext.read("$[0].expenses")).isNotEmpty();
    }
}
