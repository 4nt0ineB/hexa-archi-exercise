package lunatech.domain.port;

import io.vavr.control.Either;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> getByUsername(String username);
    Optional<User> save(User user);
    void delete(User user);
    Either<String, Todo> addTodoToUser(User user, Todo todo);
    Either<String, Todo> updateTodo(User user, Todo todo);
    Either<String, String> deleteTodo(User user, String id);
}
