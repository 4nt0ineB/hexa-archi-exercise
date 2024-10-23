package lunatech.domain.port;

import io.vavr.control.Either;
import io.vavr.control.Option;
import lunatech.domain.dto.UserInfo;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserServicePort {
    Either<String, UserInfo> find(String origin, String target);
    Either<String, List<Todo>> findTodos(String origin, String target) ;
    Either<String, List<Todo>> findTodosWithTags(String origin, String target, List<String> tags);
    Either<String, Todo> findTodoById(String origin, String username, UUID id);
    Either<String, Todo> updateTodo(String origin, String username, Todo todo);
    Either<String, Todo> addTodo(String origin, String target, Todo todo);
    Either<String, String> deleteTodo(String origin, String target, String id);
}
