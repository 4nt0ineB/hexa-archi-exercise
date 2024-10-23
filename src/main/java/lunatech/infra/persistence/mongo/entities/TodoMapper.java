package lunatech.infra.persistence.mongo.entities;

import lunatech.domain.model.Todo;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

public class TodoMapper {
    public static TodoEntity toEntity(Todo todo) {
        var todoEntity = new TodoEntity();
        todoEntity.id = todo.id().toString();
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
