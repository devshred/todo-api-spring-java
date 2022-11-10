package todo;

import static java.util.stream.StreamSupport.stream;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import todo.model.CreateTodoItem;
import todo.model.TodoItem;
import todo.model.TodoStatus;

@Service
@AllArgsConstructor
public class TodoService {
    private final TodoRepository db;

    private static TodoEntity toEntity(CreateTodoItem todoItem) {
        return new TodoEntity(todoItem.getText(), false);
    }

    public List<TodoItem> allTodoItems() {
        return stream(db.findAll().spliterator(), false).map(TodoEntity::toTodoItem).toList();
    }

    public TodoItem save(final CreateTodoItem dto) {
        return db.save(toEntity(dto)).toTodoItem();
    }

    public TodoItem findById(final UUID id) {
        return db.findById(id).map(TodoEntity::toTodoItem).orElseThrow(NotFoundException::new);
    }

    public void updateStatus(final UUID id, final TodoStatus status) {
        final TodoEntity entity = db.findById(id).orElseThrow(NotFoundException::new);
        entity.setDone(status.getDone());
        db.save(entity);
    }

    public void delete(final UUID id) {
        final TodoEntity entity = db.findById(id).orElseThrow(NotFoundException::new);
        db.delete(entity);
    }
}
