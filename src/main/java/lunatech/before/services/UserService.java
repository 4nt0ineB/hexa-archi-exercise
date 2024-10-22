package lunatech.before.services;

import io.vavr.control.Either;
import jakarta.enterprise.context.ApplicationScoped;
import lunatech.before.entities.TodoEntity;
import lunatech.before.entities.UserEntity;

import java.util.Optional;

@ApplicationScoped
public final class UserService {
    
    public Either<String, Optional<UserEntity>> queryUser(String userName) {
        return queryUser(userName, Optional.empty());
    }

    public Either<String, Optional<UserEntity>> queryUser(String userName,  Optional<UserFilter> filter) {
        Optional<UserEntity> user = UserEntity.find("username", userName).firstResultOptional();
        if (user.isPresent()) {
            return Either.right(user);
        }
        return Either.left("User not found");
    }

    public Either<String, UserEntity> removeTodo(UserEntity user, TodoEntity todo) {
        throw new AssertionError();
    }

    public Either<String, UserEntity> addTodo(UserEntity user, TodoEntity todoToAdd) {
        user.todos.add(todoToAdd);
        return Either.right(user);
    }

    public Either<String, UserEntity> updateTodo(Object value0, Object value1) {
        throw new AssertionError();
    }


}
