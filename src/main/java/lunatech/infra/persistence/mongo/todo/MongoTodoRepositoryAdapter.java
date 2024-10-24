package lunatech.infra.persistence.mongo.todo;

import jakarta.inject.Singleton;
import lunatech.domain.todo.Todo;
import lunatech.domain.todo.TodoRepositoryPort;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class MongoTodoRepositoryAdapter implements TodoRepositoryPort {

    @Override
    public List<Todo> find(String username) {
        return TodoEntity.<TodoEntity>list("username", username).stream()
                .map(TodoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Todo> findWithTags(String username, List<String> tags) {
        return TodoEntity.<TodoEntity>find("{'username': ?1, 'tags': { $all: ?2}}", username, tags)
                .stream()
                .map(TodoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Todo> findById(String username, UUID id) {
        return TodoEntity.<TodoEntity>find("{'username': ?1, '_id': ?2}", username, id.toString())
                .stream()
                .findFirst()
                .map(TodoMapper::toDomain);
    }

    @Override
    public Optional<Todo> update(String username, Todo todo) {
        return TodoEntity.<TodoEntity>find("{'username': ?1, '_id': ?2}", username, todo.id().toString())
                .stream()
                .findFirst()
                .map(todoEntity -> {
                    todoEntity.title = todo.title();
                    todoEntity.description = todo.description();
                    todoEntity.tags = todo.tags() ;
                    todoEntity.done = todo.done();
                    todoEntity.update();
                    return TodoMapper.toDomain(todoEntity);
                });
    }

    @Override
    public Todo add(String username, Todo todo) {
        var entity = TodoMapper.toEntity(username, todo);
        entity.persist();
        return TodoMapper.toDomain(entity);
    }

    @Override
    public Optional<UUID> delete(String username, UUID id) {
        System.out.println("Deleting todo with id: " + id);
        var count =  TodoEntity.delete("{'username': ?1, '_id': ?2}", username, id.toString());
        System.out.println("Deleted todos: " + count);
        return count > 0 ? Optional.of(id) : Optional.empty();
    }
}
