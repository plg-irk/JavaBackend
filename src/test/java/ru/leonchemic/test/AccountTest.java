package ru.leonchemic.test;

import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static ru.leonchemic.test.Endpoints.GET_ACCOUNT;


public class AccountTest extends BaseTest {

    @Test
    void getAccountInfoTest() {
        given(requestSpecificationWithAuth, positiveResponseSpecification)
                .get(GET_ACCOUNT, username);
    }
}
