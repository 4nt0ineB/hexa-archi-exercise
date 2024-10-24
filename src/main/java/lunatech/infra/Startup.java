package lunatech.infra;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.user.*;
import lunatech.domain.todo.Todo;
import lunatech.domain.todo.TodoRepositoryPort;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;


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
    TodoRepositoryPort todoRepository;

    @Transactional
    public void loadFixtures(@Observes StartupEvent evt) {
        logger.info("Executing fixtures startup operation");
        var users = List.of(
                new User("Nicolas", "pwd", Role.ADMIN),
                new User("Ewen", "pwd", Role.REGULAR),
                new User("Sebastien", "pwd", Role.REGULAR)
        );
        users.forEach(u -> userService.create(u));
        todoRepository.add("Ewen", new Todo(UUID.randomUUID(), "Run", List.of("sport", "health")));
    }
}