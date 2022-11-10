package todo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface TodoRepository extends CrudRepository<TodoEntity, UUID> {}
