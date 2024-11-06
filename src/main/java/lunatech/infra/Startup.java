package lunatech.infra;

import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lunatech.infra.persistence.UserFixtures;


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