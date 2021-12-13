package ru.leonchemic.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.leonchemic.test.Endpoints.*;

public class FileImageTest extends BaseTest {
    String imageID;
    RequestSpecification requestSpecificationWithFile;
    Response response;

    @Test
    void uploadFileTest() {

        requestSpecificationWithFile = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart("image", new File(
                        "src/test/resources/main-1920x1080.jpg"))
                .build();

        response = given(requestSpecificationWithFile, positiveResponseSpecification)
                .post(UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .response();

        imageID = response.jsonPath().getString("data.id");
    }

    @AfterEach
    void deleteFileTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(GET_ACCOUNT + UPLOAD_IMAGE_ID, username, imageID)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
