package lunatech.infra.persistence;

import io.quarkus.arc.profile.IfBuildProfile;
import io.vavr.control.Either;
import jakarta.inject.Singleton;
import lunatech.domain.Todo;
import lunatech.domain.User;
import lunatech.domain.UserRepositoryPort;

import java.util.Optional;

@Singleton
@IfBuildProfile("prod")
public class MongoUserRepositoryAdapter implements UserRepositoryPort {
    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public Optional<User> save(User user) {
        return Optional.empty();
    }

    @Override
    public void delete(User user) {

    }

    @Override
    public Either<String, Todo> addTodoToUser(User user, Todo todo) {
        return null;
    }
}
