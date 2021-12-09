package ru.leonchemic.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class FileImageTest extends BaseTest {
    String imageDeleteHash;

    @Test
    void uploadFileTest() {
        imageDeleteHash = given()
                .header("Authorization", token)
                .multiPart("image", new File("src/test/resources/main-1920x1080.jpg"))
                .log()
                .method()
                .log()
                .uri()
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

    @AfterEach
    void deleteFileTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{id}", username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
