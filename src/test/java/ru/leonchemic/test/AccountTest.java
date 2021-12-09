package ru.leonchemic.test;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AccountTest extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .then()
                .statusCode(200);
    }
}
