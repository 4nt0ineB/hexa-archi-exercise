package lunatech.domain.todo;

import io.vavr.control.Either;

import java.util.List;
import java.util.UUID;

public interface TodoServicePort {
    Either<String, List<Todo>> find(String origin, String target) ;
    Either<String, List<Todo>> findWithTags(String origin, String target, List<String> tags);
    Either<String, Todo> findById(String origin, String username, UUID id);
    Either<String, Todo> update(String origin, String username, Todo todo);
    Either<String, Todo> add(String origin, String target, Todo todo);
    Either<String, UUID> delete(String origin, String target, UUID id);
}
