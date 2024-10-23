package lunatech.infra;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.model.Role;
import lunatech.domain.model.Todo;
import lunatech.domain.model.User;
import lunatech.domain.port.UserRepositoryPort;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


/**
 * This class is executed everytime that we launch the application. We use it to load fixtures.
 */

@Singleton
public class Startup {
    private static final Logger logger = Logger.getLogger(Startup.class);

    @Inject
    UserRepositoryPort userRepository;

    @Transactional
    public void loadFixtures(@Observes StartupEvent evt) {
        logger.info("Executing fixtures startup operation");
        var users = List.of(
                new User("Nicolas", "pwd", Role.ADMIN),
                new User("Ewen", "pwd", Role.REGULAR),
                new User("Sebastien", "pwd", Role.REGULAR)
        );
        users.get(1).addTodoToUser(new Todo(String.valueOf(ThreadLocalRandom.current().nextLong()), "Run", List.of("sport", "health")));
        users.forEach(u -> userRepository.save(u));
    }
}