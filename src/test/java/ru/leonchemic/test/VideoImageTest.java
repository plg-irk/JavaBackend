package ru.leonchemic.test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.leonchemic.test.Endpoints.*;

public class VideoImageTest extends BaseTest {
    String imageID;
    MultiPartSpecification fileImage;
    RequestSpecification requestSpecificationWithVideo;
    Response response;

    @Test
    void uploadVideoTest() {

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

    @AfterEach
    void deleteVideoTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .delete(GET_ACCOUNT + UPLOAD_IMAGE_ID, username, imageID)
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}
