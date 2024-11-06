package lunatech.application;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import lunatech.TestProfile;
import lunatech.infra.persistence.UserFixtures;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@io.quarkus.test.junit.TestProfile(TestProfile.class)
@TestHTTPEndpoint(TodoResourceAdapter.class)
public class TodoResourceAdapterIT {

    @Inject
    UserFixtures userFixtures;

    @BeforeEach
    public void setup() {
        userFixtures.load();
    }

    @AfterEach
    public void setupAll() {
        userFixtures.clear();
    }

    @Test
    public void testGetTodosNotAuthenticated() {
        given()
                .when()
                .get()
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "Ewen")
    public void testGetTodosForbidden() {
        given()
                .queryParam("user", "Sebastien")
        .when()
                .get()
        .then()
                .statusCode(403);
    }
    @Test
    @TestSecurity(user = "Nicolas")
    public void testGetTodosAuthorized() {
        given()
                .queryParam("user", "Ewen")
                .log().all()
                .when()
                .get()
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    @TestSecurity(user = "Ewen")
    public void testAddTodoAuthorized() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                {
                    "title": "Test Todo",
                    "description": "This is a test todo",
                    "tags": ["test", "todo"]
                }
                """)
                .when()
                .post()
                .then()
                .statusCode(201)
                .body("title", equalTo("Test Todo"));
    }

}
