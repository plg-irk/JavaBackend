package ru.leonchemic.test;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

import static io.restassured.RestAssured.given;
import static ru.leonchemic.test.Endpoints.*;


public class GetBase64ImageTest extends BaseTest {
    private final String PATH_TO_FILE = "src/test/resources/main-1920x1080.jpg";
    String imageID;
    static String encodedFile;

    MultiPartSpecification fileImage;
    RequestSpecification requestSpecificationWithFile;
    Response response;

    @BeforeEach
    void setup() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

        fileImage = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        requestSpecificationWithFile = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(fileImage)
                .build();

        response = given(requestSpecificationWithFile, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();

        imageID = response.jsonPath().getString("data.id");
    }

    @Test
    void getBase64ImageFileTest() {
        given(requestSpecificationWithFile, positiveResponseSpecification)
                .get(UPLOAD_IMAGE_ID, imageID)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @AfterEach
    void teardown() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(GET_ACCOUNT + UPLOAD_IMAGE_ID, username, imageID)
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
}

