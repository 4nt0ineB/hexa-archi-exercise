package lunatech.infra;

import io.quarkus.arc.profile.IfBuildProfile;
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
import lunatech.infra.persistence.mongo.user.UserFixtures;
import org.jboss.logging.Logger;

import java.util.List;


/**
 * This class is executed everytime that we launch the application. We use it to load fixtures.
 *
 */

@Singleton
@IfBuildProfile("dev")
public class Startup {

    @Inject
    UserFixtures userFixtures;

    public void start(@Observes StartupEvent evt) {
        userFixtures.load();
    }
}