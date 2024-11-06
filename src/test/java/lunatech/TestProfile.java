package lunatech;

import io.quarkus.test.junit.QuarkusTestProfile;

public class TestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "test";
    }

    @Override
    public boolean runMainMethod() {
        return false;
    }

}