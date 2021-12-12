package ru.leonchemic.test;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;


public class UpdateBase64ImageTest extends BaseTest {
    static private final String PATH_TO_FILE = "src/test/resources/main-1920x1080.jpg";
    static String imageDeleteHash;
    static String encodedFile;

    @BeforeAll
    static void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
        imageDeleteHash = getImageDeleteHash();
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
    static void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{id}",
                        username, imageDeleteHash)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    static private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    static private String getImageDeleteHash() {
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

