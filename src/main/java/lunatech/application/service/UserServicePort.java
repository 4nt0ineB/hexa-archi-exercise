package lunatech.application.service;

import io.vavr.control.Either;
import lunatech.domain.Todo;
import lunatech.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserServicePort {
    Either<String, Optional<User>> find(String origin, String target);
    Either<String, Optional<List<Todo>>> findTodos(String origin, String target) ;
    Optional<List<Todo>> findTodosWithTags(String origin, String target, List<String> tags);
    Optional<Todo> findTodoById(String username, long id);
    Optional<User> createUser(User user);
    Optional<User> updateTodo(User user, Todo todo);
    Either<String, Todo> addTodo(User user, Todo todo);
}
