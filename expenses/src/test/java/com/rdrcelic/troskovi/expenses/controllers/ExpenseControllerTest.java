package com.rdrcelic.troskovi.expenses.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import com.rdrcelic.troskovi.expenses.dto.ExpenseDto;
import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import com.rdrcelic.troskovi.expenses.exceptions.ExpenseConflictExeption;
import com.rdrcelic.troskovi.expenses.exceptions.NoSuchExpense;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ExpensesController.class)
public class ExpenseControllerTest {
    // initialize mocks properly and enable automatic Mockito framework validation - do the same as @RunWith(MockitoJUnitRunner.class)
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpensesDao expensesDao;

    private EnhancedRandom enhancedRandom;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandom();
    }

    @Test
    public void getAllExpenses() throws Exception {
        // given
        when(expensesDao.getAllExpenses()).thenReturn(Arrays.asList(enhancedRandom.nextObject(ExpenseDto.class)));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/expenses")
                        .header("accept","application/json; charset=UTF-8");
        // then
        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expenses").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expenses[0].description").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].expenses[0].amount").exists());
    }

    @Test
    public void createNewExpenseOK() throws Exception {
        // given
        ExpenseEntity newExpense = enhancedRandom.nextObject(ExpenseEntity.class);
        newExpense.setActive(true);
        when(expensesDao.createExpense(any(ExpenseDto.class))).thenReturn(newExpense);
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/expenses")
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(ExpenseDto.class)));
        // then
        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].newExpense").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].newExpense.active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].newExpense.description").value(newExpense.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].newExpense.amount").value(newExpense.getAmount()));
    }

    @Test
    public void crateNewExpenseNOK() throws Exception {
        // given
        when(expensesDao.createExpense(any(ExpenseDto.class))).thenThrow(new ExpenseConflictExeption("Expense already exist"));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.post("/expenses")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(ExpenseDto.class)));
        // then
        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].error").exists());
    }

    @Test
    public void modifyExpenseOK() throws Exception {
        // given
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/expenses/101")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(ExpenseDto.class)));
        // when
        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isNoContent());
        // then
        verify(expensesDao).patchExpense(eq(101L), any(ExpenseDto.class));
    }

    @Test
    public void modifyExpenseNOK() throws Exception {
        // given
        doThrow(new NoSuchExpense(101,"Expense with given id doesn't exist"))
                .when(expensesDao).patchExpense(anyLong(), any(ExpenseDto.class));
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.patch("/expenses/101")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .content(objectMapper.writeValueAsString(enhancedRandom.nextObject(ExpenseDto.class)));
        // then
        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]").isMap())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].error").exists());
    }
}
