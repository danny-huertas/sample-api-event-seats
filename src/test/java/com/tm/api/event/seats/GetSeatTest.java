package com.tm.api.event.seats;

import static com.tm.api.event.seats.constants.IntegrationTestConstants.PATH_PARAM_EVENT_ID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import com.tm.api.EventSeatsApplication;
import com.tm.api.event.seats.configuration.SpringRootConfig;
import com.tm.api.event.seats.constants.IntegrationTestConstants;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventSeatsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sql/data-test.sql")

@ActiveProfiles("test")
public class GetSeatTest {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.basePath = SpringRootConfig.REQUEST_MAPPING;
    }

    @Test
    public void getSeatCountAllSuccessTest() {
        RestAssured.given().when().pathParam(PATH_PARAM_EVENT_ID, IntegrationTestConstants.EVENT_ID).when()
                .get(IntegrationTestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_OK)
                .body("seatCount", equalTo(100)).body(IntegrationTestConstants.OPERATION_RESULT, equalTo("OK"))
                .body(IntegrationTestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body("operation.errors.size()", equalTo(0))
                .body(IntegrationTestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void getSeatCountInvalidAcceptLanguageTest() {
        RestAssured.given().when().header("Accept-Language", "invalid123")
                .pathParam(PATH_PARAM_EVENT_ID, IntegrationTestConstants.EVENT_ID)
                .get(IntegrationTestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(IntegrationTestConstants.OPERATION_RESULT, equalTo("ERROR"))
                .body(IntegrationTestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_CODE, equalTo("acceptLanguageHeaderInvalid"))
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_MESSAGE,
                        equalTo("Request header Accept-Language is not in the expected format."))
                .body(IntegrationTestConstants.OPERATION_ERRORS_MORE_INFO, equalTo(""))
                .body(IntegrationTestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void getSeatCountInvalidEventIdTest() {
        // MethodArgumentTypeMismatchException. expected Long type but passed in value is String.
        RestAssured.given().when().pathParam(PATH_PARAM_EVENT_ID, "invalidEventId")
                .get(IntegrationTestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(IntegrationTestConstants.OPERATION_RESULT, equalTo("ERROR"))
                .body(IntegrationTestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_CODE, equalTo("pathParameterInvalid"))
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_MESSAGE,
                        equalTo("Resource URI parameter is unable to be processed."))
                .body(IntegrationTestConstants.OPERATION_ERRORS_MORE_INFO, equalTo(""))
                .body(IntegrationTestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void getSeatCountMethodNotAllowedTest() {
        // Request method 'PATCH' not supported
        RestAssured.given().when().pathParam(PATH_PARAM_EVENT_ID, IntegrationTestConstants.EVENT_ID)
                .patch(IntegrationTestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }
}
