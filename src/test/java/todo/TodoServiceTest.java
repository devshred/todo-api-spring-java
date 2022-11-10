package todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import todo.model.CreateTodoItem;
import todo.model.TodoItem;
import todo.model.TodoStatus;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {
    private static final String TODO_TEXT = "some todo";
    private static final UUID RANDOM_ID = UUID.randomUUID();
    @Mock
    private TodoRepository db;

    @InjectMocks
    private TodoService todoService;

    private static TodoEntity createTodoEntity() {
        return new TodoEntity(TODO_TEXT, false);
    }

    @Test
    void allTodoItems() {
        final Iterable<TodoEntity> entities =
                () -> List.of(createTodoEntity(), createTodoEntity(), createTodoEntity()).iterator();
        when(db.findAll()).thenReturn(entities);

        final List<TodoItem> todoItems = todoService.allTodoItems();

        assertThat(todoItems).hasSize(3);
        verify(db).findAll();
    }

    @Test
    void save() {
        when(db.save(any())).thenReturn(createTodoEntity());

        todoService.save(new CreateTodoItem().text(TODO_TEXT));

        verify(db).save(any());
    }

    @Test
    void findById() {
        when(db.findById(RANDOM_ID)).thenReturn(Optional.of(createTodoEntity()));

        final TodoItem actual = todoService.findById(RANDOM_ID);

        assertThat(actual.getText()).isEqualTo(TODO_TEXT);
    }

    @Test
    void findById_entityDoesNotExist() {
        when(db.findById(RANDOM_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.findById(RANDOM_ID)) //
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateStatus() {
        final ArgumentCaptor<TodoEntity> captor = ArgumentCaptor.forClass(TodoEntity.class);
        when(db.findById(RANDOM_ID)).thenReturn(Optional.of(createTodoEntity()));

        todoService.updateStatus(RANDOM_ID, new TodoStatus().done(true));

        verify(db).save(captor.capture());
        assertThat(captor.getValue().getDone()).isTrue();
    }

    @Test
    void delete() {
        when(db.findById(RANDOM_ID)).thenReturn(Optional.of(createTodoEntity()));

        todoService.delete(RANDOM_ID);

        verify(db).delete(any());
    }
}