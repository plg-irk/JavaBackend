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

public class UpdateVideoImageTest extends BaseTest {
    static String imageID;
    RequestSpecification requestSpecificationWithVideo;
    Response response;

    @BeforeEach
    void setup() {


        requestSpecificationWithVideo = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart("video", new File(
                        "src/test/resources/pexels-nadezhda-moryak-6790484.mp4"))
                .build();

        response = given(requestSpecificationWithVideo, positiveResponseSpecification)
                .post(UPLOAD)
                .prettyPeek()
                .then()
                .extract()
                .response();

        imageID = response.jsonPath().getString("data.id");
    }

    @Test
    void getVideoTest() {
        given(requestSpecificationWithVideo, positiveResponseSpecification)
                .get(GET_ACCOUNT+UPLOAD_IMAGE_ID, username, imageID)
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
