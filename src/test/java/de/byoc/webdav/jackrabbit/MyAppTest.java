package de.byoc.webdav.jackrabbit;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.jackrabbit.webdav.DavMethods;
import org.apache.jackrabbit.webdav.WebdavRequest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
public class MyAppTest {

  private static final Logger LOG = LoggerFactory.getLogger(MyAppTest.class);

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
  void getStuff() {
    given()
            .log().all()
            .auth().basic("admin", "admin")
            .accept(ContentType.JSON)
            .when()
            .get("/repository/default/world_12.json")
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