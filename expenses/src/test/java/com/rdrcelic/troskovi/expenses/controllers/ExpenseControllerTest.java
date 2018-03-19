package com.rdrcelic.troskovi.expenses.controllers;

import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ExpensesController.class)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpensesDao expensesDao;

    private EnhancedRandom enhancedRandom;

    @Before
    public void setup() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    }

    @Test
    public void getAllActiveExpenses() throws Exception {

    }

    @Test
    public void getAllExpenses() throws Exception {
        // given
        when(expensesDao.getAllExpenses()).thenReturn(Arrays.asList(enhancedRandom.nextObject(ExpenseDto.class)));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/expenses")
                        .header("accept","application/json; charset=UTF-8");
        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expenses").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expenses[0].expenseDescription").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expenses[0].amount").exists());
    }

    @Test
    public void createNewExpenseOK() throws Exception {

    }

    @Test
    public void crateNewExpenseNOK() throws Exception {

    }

    @Test
    public void modifyExpenseOK() throws Exception {

    }

    @Test
    public void modifyExpenseNOK() throws Exception {

    }

    @Test
    public void makeExpenseInactive() throws Exception {

    }
}
