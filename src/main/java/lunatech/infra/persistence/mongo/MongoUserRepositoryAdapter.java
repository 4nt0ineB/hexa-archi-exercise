package lunatech.infra.persistence.mongo;

import io.quarkus.arc.profile.IfBuildProfile;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import lunatech.infra.persistence.mongo.entities.TodoMapper;
import lunatech.infra.persistence.mongo.entities.UserEntity;
import lunatech.infra.persistence.mongo.entities.UserMapper;

import java.util.Optional;

@Singleton
@IfBuildProfile("prod")
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
    public void delete(User user) {
        UserEntity.delete("username", user.username());
    }

    @Override
    public Either<String, Todo> addTodoToUser(User user, Todo todo) {
        return UserEntity.<UserEntity>find("username", user.username())
                .firstResultOptional()
                .map(userEntity -> {
                    userEntity.todos.add(TodoMapper.toEntity(todo));
                    userEntity.persist();
                    return Either.<String, Todo>right(todo);
                })
                .orElseGet(() -> Either.left("User not found"));
    }
}
