package lunatech.application;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.ws.rs.core.MediaType;
import lunatech.DevTestProfile;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@TestProfile(DevTestProfile.class)
public class TodoResourceAdapterIT {


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
