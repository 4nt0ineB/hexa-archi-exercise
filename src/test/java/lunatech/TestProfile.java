package lunatech;

import io.quarkus.test.common.ArtifactLauncher;
import io.quarkus.test.junit.QuarkusTestProfile;
import jakarta.inject.Inject;
import lunatech.infra.persistence.mongo.user.UserFixtures;

public class TestProfile implements QuarkusTestProfile {

    @Inject
    UserFixtures userFixtures;

    @Override
    public String getConfigProfile() {
        return "test";
    }

    @Override
    public boolean runMainMethod() {
        return false;
    }

}