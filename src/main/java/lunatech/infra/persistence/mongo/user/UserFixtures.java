package lunatech.infra.persistence.mongo.user;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.todo.Todo;
import lunatech.domain.todo.TodoInput;
import lunatech.domain.todo.TodoServicePort;
import lunatech.domain.user.Role;
import lunatech.domain.user.User;
import lunatech.domain.user.UserRepositoryPort;
import lunatech.domain.user.UserServicePort;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserFixtures {

    private static final Logger logger = Logger.getLogger(UserFixtures.class);

    @Inject
    UserServicePort userService;

    @Inject
    TodoServicePort todoService;

    @Inject
    UserRepositoryPort userRepository;

    List<Todo> todos = new ArrayList<>();
    List<User> users = List.of(
            new User("Nicolas", "pwd", Role.ADMIN),
            new User("Ewen", "pwd", Role.REGULAR),
            new User("Sebastien", "pwd", Role.REGULAR)
    );

    @Transactional
    public void load() {
        logger.info("Executing user fixtures");
        users.forEach(u -> userService.create(u));
        var todo = todoService.add("Ewen", "Ewen", new TodoInput("Run", "", List.of("sport", "health")));
        todos.add(todo);
    }

    public void clear() {
        logger.info("Clearing user fixtures");
        todoService.delete("Ewen", "Ewen", todos.get(0).id());
        todos.clear();
        users.forEach(u -> userRepository.delete(u.username()));
    }
}
