package lunatech.infra.persistence;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.permission.PermissionManager;
import lunatech.domain.todo.Todo;
import lunatech.domain.todo.TodoInput;
import lunatech.domain.todo.TodoServicePort;
import lunatech.domain.user.Role;
import lunatech.domain.user.User;
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
    PermissionManager permissionManager;

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
        var context = permissionManager.as("Ewen").getAccess();
        var todoInput = new TodoInput("Run", "", List.of("sport", "health"));
        var todo = todoService.add(context, todoInput);
        todos.add(todo);
    }

    public void clear() {
        logger.info("Clearing user fixtures");
        var context = permissionManager.as("Ewen").getAccess();
        todoService.delete(context, todos.get(0).id());
        todos.clear();
        var contextBuilder = permissionManager.as("Nicolas");
        for (var user : users) {
            contextBuilder.impersonate(user.username());
            userService.delete(contextBuilder.getAccess());
        }
    }
}
