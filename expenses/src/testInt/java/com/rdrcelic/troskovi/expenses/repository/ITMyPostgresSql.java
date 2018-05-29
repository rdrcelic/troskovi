package com.rdrcelic.troskovi.expenses.repository;

import com.rdrcelic.troskovi.expenses.TroskoviApplication;
import com.rdrcelic.troskovi.expenses.entities.ExpenseEntity;
import org.assertj.core.api.Assertions;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * This is integration test with test containers
 * @see <a href="https://www.testcontainers.org/usage/database_containers.html#jdbc-url">Testcontainers.org</a>
 */
@ActiveProfiles("integrationtest")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TroskoviApplication.class)
@ContextConfiguration(initializers = ITMyPostgresSql.Initializer.class)
public class ITMyPostgresSql {
    // starts DB before the test and tears it down after the test
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            EnvironmentTestUtils.addEnvironment("testcontainers", applicationContext.getEnvironment(),

                    "spring.datasource.url=jdbc:tc:postgresql://localhost:" + dbContainer.getExposedPorts().get(0)
                            + "/test?TC_INITSCRIPT=init_test_expenses_db.sql",
                    "spring.datasource.username=" + dbContainer.getUsername(),
                    "spring.datasource.password=" + dbContainer.getPassword(),
                    "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver");
        }
    }

    @ClassRule
    public static PostgreSQLContainer dbContainer = new PostgreSQLContainer();

    @Autowired
    private ExpensesRepository repository;

    @Test
    public void testFindExpense() {
        // DB initialization should take place via TC_INITSCRIPT (ref. initialize method above)
        // given
//        ExpenseEntity newEntity = new ExpenseEntity();
//        newEntity.setDescription("hrana");
//        newEntity.setAmount(BigDecimal.valueOf(7.45));
//        repository.save(newEntity);
        // when
        Iterable<ExpenseEntity> expenseEntity = repository.findAll();

        // then
        Assertions.assertThat(expenseEntity).isNotNull();

    }
}
