package lunatech.infra.persistence.mongo.todo;

import lunatech.domain.todo.Todo;

import java.util.List;
import java.util.UUID;

public class TodoMapper {
    public static TodoEntity toEntity(String username, Todo todo) {
        var todoEntity = new TodoEntity();
        todoEntity.id = todo.id().toString();
        todoEntity.username = username;
        todoEntity.title = todo.title();
        todoEntity.description = todo.description();
        todoEntity.done = todo.done();
        return todoEntity;
    }

    public static Todo toDomain(TodoEntity entity) {
        return new Todo(UUID.fromString(entity.id),
                entity.title,
                entity.description,
                entity.tags == null ? List.of() : entity.tags,
                entity.done);
    }
}
