package com.rdrcelic.troskovi.expenses.repository;

import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.MSSQLServerContainer;

/**
 * This is integration test with test containers
 * @see <a href="https://www.testcontainers.org/usage/database_containers.html#jdbc-url">Testcontainers.org</a>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = ITMySQ.Initializer.class)
//@TestPropertySource(
//        locations = "classpath:application-integrationtest.yml")
public class ITMySQ {
    @ClassRule
    public static MSSQLServerContainer mysqlserver = new MSSQLServerContainer();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues
                    .of("spring.datasource.url",
                            "jdbc:tc:mysql://localhost:" +
                                    mysqlserver.getExposedPorts().get(0) +
                                    "/test?TC_INITSCRIPT=init_test_expenses_db.sql",
                        "spring.datasource.username", mysqlserver.getUsername(),
                        "spring.datasource.password", mysqlserver.getPassword(),
                        "spring.datasource.driver-class-name", "org.testcontainers.jdbc.ContainerDatabaseDriver")
                    .applyTo(applicationContext.getEnvironment(), TestPropertyValues.Type.MAP, "testcontainers");
        }
    }

    @Autowired
    private ExpensesRepository repository;

    @Test
    public void testFindExpense() {
        ExpenseEntity expenseEntity = repository.findById(1L).get();

        Assertions.assertThat(expenseEntity).isNotNull();

    }
}
