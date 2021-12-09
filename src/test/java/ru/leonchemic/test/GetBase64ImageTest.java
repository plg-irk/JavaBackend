package ru.leonchemic.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import static io.restassured.RestAssured.given;


public class GetBase64ImageTest extends BaseTest {
    private final String PATH_TO_FILE = "src/test/resources/main-1920x1080.jpg";
    String imageDeleteHash;
    static String encodedFile;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
        imageDeleteHash = getImageDeleteHash();
    }

    @Test
    void getBase64ImageFileTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}/image/{id}",
                        username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{id}",
                        username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    private String getImageDeleteHash() {
        imageDeleteHash = given()
                .headers("Authorization", token)
                .multiPart("image", encodedFile)
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.id");
        return imageDeleteHash;
    }
}

