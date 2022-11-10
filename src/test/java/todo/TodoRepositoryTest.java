package todo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.google.common.collect.ImmutableList;

@DataJpaTest(properties = { //
        "spring.test.database.replace=NONE", //
        "spring.datasource.url=jdbc:tc:postgresql:13.8-alpine:///todo" })
class TodoRepositoryTest {
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
