package com.rdrcelic.troskovi.expenses.logging;

import com.rdrcelic.troskovi.expenses.controllers.ExpensesController;
import com.rdrcelic.troskovi.expenses.dao.ExpensesDao;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ExpensesController.class, secure = false)
@EnableAspectJAutoProxy
@Import(ControllerLoggingAspect.class)
public class ControllerLoggingAspectTest {
    // initialize mocks properly and enable automatic Mockito framework validation
    // do the same as @RunWith(MockitoJUnitRunner.class) which couldn't be applied here due to @WebMvcTest
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExpensesDao expensesDao;

    @After
    public void cleanup() {
        TestLogAppender.clearEvents();
    }

    @Test
    public void testLoggingAspectForSuccessfullRequest() throws Exception {
        // given
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/expenses")
                        .header("accept","application/json; charset=UTF-8");
        // when
        mockMvc.perform(builder);
        // then
        assertThat(TestLogAppender.getEvents())
                .extracting("formattedMessage")
                .contains("Entering method: getAllExpenses", "Exiting method: getAllExpenses");
    }

    @Test
    public void testLoggingAspectWhenExceptionThrown() throws Exception {
        // given
        when(expensesDao.getAllExpenses()).thenThrow(RuntimeException.class);
        MockHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.get("/expenses")
                        .header("accept","application/json; charset=UTF-8");
        // when
        try {
            mockMvc.perform(builder);
        } catch (Exception e) {
            // ignore
        }
        assertThat(TestLogAppender.getEvents())
                .extracting("formattedMessage")
                .contains("Entering method: getAllExpenses"
                , "Exception java.lang.RuntimeException occured in method: getAllExpenses");
    }
}
