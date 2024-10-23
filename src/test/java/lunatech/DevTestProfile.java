package lunatech;

import io.quarkus.test.junit.QuarkusTestProfile;

public class DevTestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "dev";
    }

}