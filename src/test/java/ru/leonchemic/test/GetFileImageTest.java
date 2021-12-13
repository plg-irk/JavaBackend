package ru.leonchemic.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.leonchemic.test.Endpoints.*;

public class GetFileImageTest extends BaseTest {
    static String imageID;
    RequestSpecification requestSpecificationWithFile;
    Response response;


    @BeforeEach
    void setup() {
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

    @Test
    void getFileTest() {
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
}
