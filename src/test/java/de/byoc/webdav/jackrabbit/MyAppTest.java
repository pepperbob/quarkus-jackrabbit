package de.byoc.webdav.jackrabbit;

import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import javax.inject.Inject;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
public class MyAppTest {

    private static final Logger LOG = LoggerFactory.getLogger(MyAppTest.class);

    @Inject
    Repository repo;

    @Test
    void testHelloEndpoint() {
        given()
            .body("Hello World!")
            .when()
            .post("/hello/datei.txt")
            .then()
            .statusCode(202);
    }

    @Test
    void getStuff() throws RepositoryException {
        given()
            .body(Map.of("test", "here we roll!"))
            .contentType(ContentType.JSON)
            .when()
            .post("/hello/world_12.json")
            .then()
            .statusCode(202);

        given()
            .log().all()
            .auth().basic("admin", "admin")
            .accept(ContentType.JSON)
            .when()
            .get("/repository/default/abc/world_12.json")
            .then()
            .log().all()
            .statusCode(200);
    }

    @Test
    void putStuff() throws IOException, URISyntaxException {
        given()
            .log().all()
            .auth().basic("admin", "admin")
            .body(Map.of("test", "abc"))
            .contentType(ContentType.JSON)
            .when()
            .put("/repository/default/world_13.json")
            .then()
            .log().all()
            .statusCode(201);


    }


    @Test
    void verbs() {
        given()
            .auth().basic("admin", "admin")
            .log().all()
            .body(Map.of("test", "abc"))
            .when()
            .request("PROPFIND", "/repository/default/world_1.json")
            .then()
            .log().all()
            .statusCode(201);
    }

}
