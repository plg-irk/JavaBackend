package ru.leonchemic.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class GetFileImageTest extends BaseTest {
    String imageDeleteHash;

    @BeforeEach
    void uploadFileTest() {
        imageDeleteHash = given()
                .header("Authorization", token)
                .multiPart("image", new File("src/test/resources/main-1920x1080.jpg"))
                .expect()
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @Test
    void getFileTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}/image/{deletehash}",
                        username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @AfterEach
    void deleteFileTest() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deletehash}", username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);

    }

}
