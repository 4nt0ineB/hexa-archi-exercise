package lunatech;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lunatech.domain.Role;
import lunatech.domain.User;
import lunatech.infra.persistence.InMemoryUserRepositoryAdapter;
import org.jboss.logging.Logger;

import java.util.List;


/**
 * This class is executed everytime that we launch the application. We use it to load fixtures.
 */

@Singleton
public class Startup {
    private static final Logger logger = Logger.getLogger(Startup.class);

    private final List<User> users = List.of(
            new User("Nicolas", "pwd", Role.ADMIN),
            new User("Ewen", "pwd", Role.REGULAR),
            new User("Sebastien", "pwd", Role.REGULAR)
    );

    @Inject
    InMemoryUserRepositoryAdapter userRepository;

    @Transactional
    public void loadFixtures(@Observes StartupEvent evt) {
        logger.info("Executing fixtures startup operation");
        users.forEach(u -> userRepository.save(u));
//        userRepository.getByUsername("Nicolas").ifPresent(user -> {
//            System.out.println("user: " + user);
//        });
    }
}

/*
@Singleton
public class Startup {
    private static final Logger logger = Logger.getLogger(Startup.class);

    private final List<UserEntity> users = List.of(
            new UserEntity("Nicolas", "pwd", Role.ADMIN, new ArrayList<>()),
            new UserEntity("Ewen", "pwd", Role.REGULAR, new ArrayList<>()),
            new UserEntity("Sebastien", "pwd", Role.REGULAR, new ArrayList<>())
    );

    @Transactional
    public void loadFixtures(@Observes StartupEvent evt) {
        logger.info("Executing fixtures startup operation");

        UserEntity.deleteAll();
        users.forEach(u -> u.persist());
    }
}*/
