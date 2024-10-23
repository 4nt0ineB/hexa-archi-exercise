package lunatech.infra.persistence.mongo;

import io.quarkus.arc.profile.IfBuildProfile;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.infra.persistence.mongo.entities.TodoMapper;
import lunatech.infra.persistence.mongo.entities.UserEntity;
import lunatech.infra.persistence.mongo.entities.UserMapper;

import java.util.Optional;
import java.util.UUID;

@Singleton
@IfBuildProfile("dev")
public class MongoUserRepositoryAdapter implements UserRepositoryPort {

    @Override
    public Optional<User> getByUsername(String username) {
        return UserEntity.<UserEntity>find("username", username)
                .firstResultOptional()
                .map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> save(User user) {
        var entity = UserMapper.toEntity(user);
        UserEntity.persist(entity);
        return Optional.of(UserMapper.toDomain(entity));
    }

    @Override
    public Either<String, Todo> addTodoToUser(User user, Todo todo) {
        return UserEntity.<UserEntity>find("username", user.username())
                .firstResultOptional()
                .map(userEntity -> {
                    userEntity.todos.add(TodoMapper.toEntity(todo));
                    userEntity.persistOrUpdate();
                    return Either.<String, Todo>right(todo);
                })
                .orElseGet(() -> Either.left("User not found"));
    }

    @Override
    public Either<String, Todo> updateTodo(User user, Todo todo) {
        return UserEntity.<UserEntity>find("username", user.username())
                .firstResultOptional()
                .map(userEntity -> {
                    var todoEntity = userEntity.todos.stream()
                            .filter(t -> t.id.equals(todo.id().toString()))
                            .findFirst();
                    if(todoEntity.isEmpty()) {
                        return Either.<String, Todo>left("Todo not found");
                    }
                    todoEntity.ifPresent(t -> {
                                t.title = todo.title();
                                t.description = todo.description();
                                t.tags = todo.tags();
                                t.done = todo.done();
                            });
                    userEntity.persistOrUpdate();
                    return Either.<String, Todo>right(todo);
                })
                .orElseGet(() -> Either.left("User not found"));
    }

    @Override
    public Either<String, UUID> deleteTodo(User user, UUID id) {
        return UserEntity.<UserEntity>find("username", user.username())
                .firstResultOptional()
                .map(userEntity -> {
                    var todoEntity = userEntity.todos.stream()
                            .filter(t -> t.id.equals(id.toString()))
                            .findFirst();
                    if(todoEntity.isEmpty()) {
                        return Either.<String, UUID>left("Todo not found");
                    }
                    todoEntity.ifPresent(t -> {
                        userEntity.todos.remove(t);
                    });
                    userEntity.persistOrUpdate();
                    return Either.<String, UUID>right(id);
                })
                .orElseGet(() -> Either.left("User not found"));
    }
}
