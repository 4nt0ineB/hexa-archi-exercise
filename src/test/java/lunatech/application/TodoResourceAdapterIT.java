package lunatech.application;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import lunatech.TestProfile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@io.quarkus.test.junit.TestProfile(TestProfile.class)
public class TodoResourceAdapterIT {

    @BeforeAll
    public static void setup() {

    }

    @Test
    public void testGetTodosNotAuthenticated() {
        given()
                .when()
                .get("/api/todos")
                .then()
                .statusCode(401);
    }

    @Test
    public void testGetTodosForbidden() {
        given()
                .auth().basic("Ewen", "pwd")
                .queryParam("user", "Sebastien")
        .when()
                .get("/api/todos")
        .then()
                .statusCode(403);
    }

    @Test
    public void testGetTodosAuthorized() {
        given()
                .auth().basic("Nicolas", "pwd")
                .queryParam("user", "Ewen")
                .when()
                .get("/api/todos")
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
                .post("/api/todos")
                .then()
                .statusCode(201)
                .body("title", equalTo("Test Todo"));
    }
}
