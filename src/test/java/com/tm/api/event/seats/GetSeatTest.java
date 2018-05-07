package com.tm.api.event.seats;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import com.tm.api.EventSeatsApplication;
import com.tm.api.common.api.Result;
import com.tm.api.event.seats.configuration.SpringRootConfig;
import com.tm.api.event.seats.constants.IntegrationTestConstants;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventSeatsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:sql/data-test.sql")

@ActiveProfiles("test")
public class GetSeatTest {

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void initBeforeClass() {
        RestAssured.basePath = SpringRootConfig.REQUEST_MAPPING;
    }

    @Test
    public void eventOneSeatCountAll() {
        //run assertions
        apiAssertions(IntegrationTestConstants.EVENT_ONE_ID, new LinkedHashMap<>(), 100);
    }

    @Test
    public void eventOneSeatCountAllAvailable() {
        Map<String, Object> requestParams = new LinkedHashMap<>();
        requestParams.put(IntegrationTestConstants.PARAM_IS_AVAILABLE, true);
        requestParams.put(IntegrationTestConstants.PARAM_IS_AISLE, false);
        requestParams.put(IntegrationTestConstants.PARAM_SEAT_TYPE, "child");

        //run assertions
        apiAssertions(IntegrationTestConstants.EVENT_ONE_ID, requestParams, 100);
    }

    @Test
    public void eventOneInvalidSeatType() {
        Map<String, Object> requestParams = new LinkedHashMap<>();
        requestParams.put(IntegrationTestConstants.PARAM_IS_AVAILABLE, true);
        requestParams.put(IntegrationTestConstants.PARAM_IS_AISLE, false);
        requestParams.put(IntegrationTestConstants.PARAM_SEAT_TYPE, "child");

        //run assertions
        //apiAssertions(IntegrationTestConstants.EVENT_ONE_ID, requestParams, 100);
    }

    @Test
    public void getSeatCountInvalidAcceptLanguage() {
        RestAssured.given().port(port).when().header("Accept-Language", "invalid123")
                .pathParam(IntegrationTestConstants.PATH_PARAM_EVENT_ID, IntegrationTestConstants.EVENT_ONE_ID)
                .get(IntegrationTestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(IntegrationTestConstants.OPERATION_RESULT, equalTo(String.valueOf(Result.ERROR)))
                .body(IntegrationTestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_CODE, equalTo("acceptLanguageHeaderInvalid"))
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_MESSAGE,
                        equalTo("Request header Accept-Language is not in the expected format."))
                .body(IntegrationTestConstants.OPERATION_ERRORS_MORE_INFO, equalTo(""))
                .body(IntegrationTestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void getSeatCountInvalidEventId() {
        // MethodArgumentTypeMismatchException. expected Long type but passed in value is String.
        RestAssured.given().port(port).when().pathParam(IntegrationTestConstants.PATH_PARAM_EVENT_ID, "invalidEventId")
                .get(IntegrationTestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(IntegrationTestConstants.OPERATION_RESULT, equalTo(String.valueOf(Result.ERROR)))
                .body(IntegrationTestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_CODE, equalTo("pathParameterInvalid"))
                .body(IntegrationTestConstants.OPERATION_ERRORS_ERROR_MESSAGE,
                        equalTo("Resource URI parameter is unable to be processed."))
                .body(IntegrationTestConstants.OPERATION_ERRORS_MORE_INFO, equalTo(""))
                .body(IntegrationTestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void getSeatCountMethodNotAllowed() {
        // Request method 'PATCH' not supported
        RestAssured.given().port(port).when()
                .pathParam(IntegrationTestConstants.PATH_PARAM_EVENT_ID, IntegrationTestConstants.EVENT_ONE_ID)
                .patch(IntegrationTestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * Runs assertions against the API for a given event id with optional query params
     *
     * @param eventId event id
     * @param requestParams map of query params
     * @param seatCount expected seat count
     */
    private void apiAssertions(Long eventId, Map<String, Object> requestParams, int seatCount) {
        RestAssured.given().port(port).when().pathParam(IntegrationTestConstants.PATH_PARAM_EVENT_ID, eventId)
                .params(requestParams).when().get(IntegrationTestConstants.COUNT_END_POINT).then()
                .statusCode(HttpStatus.SC_OK).body(IntegrationTestConstants.SEAT_COUNT, equalTo(seatCount))
                .body(IntegrationTestConstants.OPERATION_RESULT, equalTo(String.valueOf(Result.OK)))
                .body(IntegrationTestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(IntegrationTestConstants.OPERATION_ERRORS_SIZE, equalTo(0))
                .body(IntegrationTestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }
}
