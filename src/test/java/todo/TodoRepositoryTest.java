package todo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.google.common.collect.ImmutableList;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TodoRepositoryTest {
    @Container
    private static final PostgreSQLContainer database = new PostgreSQLContainer("postgres:13.9-alpine") //
            .withDatabaseName("todo") //
            .withPassword("todo") //
            .withUsername("todo");

    @DynamicPropertySource
    private static void setDatasourceProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        propertyRegistry.add("spring.datasource.password", database::getPassword);
        propertyRegistry.add("spring.datasource.username", database::getUsername);
    }

    @Autowired
    private TodoRepository db;

    @Test
    void crudOperations() {
        db.save(new TodoEntity("some todo", false));

        final List<TodoEntity> todoEntities = ImmutableList.copyOf(db.findAll());

        assertThat(todoEntities).hasSize(1);

        db.delete(todoEntities.get(0));

        assertThat(db.findAll()).isEmpty();
    }
}
