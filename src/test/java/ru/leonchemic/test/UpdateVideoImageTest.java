package ru.leonchemic.test;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class UpdateVideoImageTest extends BaseTest {
    static String imageDeleteHash;

    @BeforeAll
    static void uploadFileTest() {
        imageDeleteHash = given()
                .header("Authorization", token)
                .multiPart("image", new File(
                        "src/test/resources/pexels-nadezhda-moryak-6790484.mp4"))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
    }

    @Test
    void updateFileTest() {
        given()
                .header("Authorization", token)
                .param("title", "Lake Baikal")
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image/{id}",
                        imageDeleteHash)
                .prettyPeek();
    }

    @AfterEach
    void getFileTest() {
        Response response = given()
                .header("Authorization", token)
                .when()
                .get("https://api.imgur.com/3/account/{username}/image/{id}",
                        username, imageDeleteHash)
                .prettyPeek();
        System.out.println(response.jsonPath());
        assertThat(response.jsonPath().get("data.title"), equalTo("Lake Baikal"));
    }

    @AfterAll
    static void deleteFileTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{id}",
                        username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
