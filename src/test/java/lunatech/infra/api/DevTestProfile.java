package lunatech.infra.api;

import io.quarkus.test.junit.QuarkusTestProfile;
import jakarta.inject.Inject;
import lunatech.domain.UserRepositoryPort;

public class DevTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "dev";
    }

}