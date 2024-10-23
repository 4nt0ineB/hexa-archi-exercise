package lunatech.infra.persistence.mongo.entities;

import lunatech.domain.model.Todo;

public class TodoMapper {
    public static TodoEntity toEntity(Todo todo) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.title = todo.title();
        todoEntity.description = todo.description();
        todoEntity.done = todo.done();
        return todoEntity;
    }

    public static Todo toDomain(TodoEntity entity) {
        return new Todo(entity.todoId.toString(), entity.title, entity.description, entity.tags, entity.done);
    }
}
