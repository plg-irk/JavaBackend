package ru.leonchemic.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class VideoImageTest extends BaseTest {
    String imageDeleteHash;

    @Test
    void uploadFileTest() {
        imageDeleteHash = given()
                .header("Authorization", token)
                .multiPart("video", new File(
                        "src/test/resources/pexels-nadezhda-moryak-6790484.mp4"))
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
                .getString("data.deletehash");
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
