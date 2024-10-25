package lunatech.infra;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.todo.TodoDTO;
import lunatech.domain.todo.TodoServicePort;
import lunatech.domain.user.Role;
import lunatech.domain.user.User;
import lunatech.domain.user.UserServicePort;
import org.jboss.logging.Logger;

import java.util.List;


/**
 * This class is executed everytime that we launch the application. We use it to load fixtures.
 *
 */

@Singleton
public class Startup {
    private static final Logger logger = Logger.getLogger(Startup.class);

    @Inject
    UserServicePort userService;

    @Inject
    TodoServicePort todoService;

    @Transactional
    public void loadFixtures(@Observes StartupEvent evt) {
        logger.info("Executing fixtures startup operation");
        var users = List.of(
                new User("Nicolas", "pwd", Role.ADMIN),
                new User("Ewen", "pwd", Role.REGULAR),
                new User("Sebastien", "pwd", Role.REGULAR)
        );
        users.forEach(u -> userService.create(u));
        todoService.add("Ewen", "Ewen", new TodoDTO("Run", "", List.of("sport", "health")));
    }
}