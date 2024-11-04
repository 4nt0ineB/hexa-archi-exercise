package lunatech.application;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import lunatech.TestProfile;
import lunatech.infra.persistence.mongo.user.UserFixtures;
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
    public void testGetTodosForbidden() {
        given()
                .auth().basic("Ewen", "pwd")
                .queryParam("user", "Sebastien")
        .when()
                .get()
        .then()
                .statusCode(403);
    }
    @Test
    public void testGetTodosAuthorized() {
        given()
                .auth().basic("Nicolas", "pwd")
                .queryParam("user", "Ewen")
                .when()
                .get()
                .peek()
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));
    }

    @Test
    public void testAddTodoAuthorized() {
        given()
                .auth().basic("Ewen", "pwd")
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
