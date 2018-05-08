package com.tm.api.event.seats;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import com.tm.api.EventSeatsApplication;
import com.tm.api.common.operation.Result;
import com.tm.api.event.seats.configuration.SpringRootConfig;
import com.tm.api.event.seats.constants.TestConstants;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventSeatsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class EventSeatsIntegrationTests {

    @LocalServerPort
    private int port;
    private Map<String, Object> requestParams;

    @BeforeClass
    public static void initClass() {
        RestAssured.basePath = SpringRootConfig.REQUEST_MAPPING;
    }

    @Before
    public void initTest() {
        requestParams = new LinkedHashMap<>();
    }

    @Test
    public void seatCountTotalNoFilters() {
        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, new LinkedHashMap<>(), 90);
        apiAssertions(TestConstants.EVENT_TWO_ID, new LinkedHashMap<>(), 130);
        apiAssertions(TestConstants.EVENT_THREE_ID, new LinkedHashMap<>(), 80);
        apiAssertions(TestConstants.EVENT_FOUR_ID, new LinkedHashMap<>(), 0);
    }

    @Test
    public void seatCountTotalAvailable() {
        requestParams.put(TestConstants.PARAM_IS_AVAILABLE, true);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 30);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 60);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 80);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalNotAvailable() {
        requestParams.put(TestConstants.PARAM_IS_AVAILABLE, false);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 60);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 70);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalAisle() {
        requestParams.put(TestConstants.PARAM_IS_AISLE, true);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 60);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 30);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalNotAisle() {
        requestParams.put(TestConstants.PARAM_IS_AISLE, false);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 30);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 130);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 50);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalTypeChild() {
        requestParams.put(TestConstants.PARAM_SEAT_TYPE, TestConstants.SEAT_TYPE_CHILD);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 30);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 60);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 30);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalTypeAdult() {
        requestParams.put(TestConstants.PARAM_SEAT_TYPE, TestConstants.SEAT_TYPE_ADULT);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 60);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 70);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 50);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalInvalidType() {
        requestParams.put(TestConstants.PARAM_SEAT_TYPE, "invalid seat type");

        // IllegalStateException: Unsupported type doesnt exist.
        RestAssured.given().port(port).when().pathParam(TestConstants.PATH_PARAM_EVENT_ID, TestConstants.EVENT_ONE_ID)
                .params(requestParams).when().get(TestConstants.COUNT_END_POINT).then()
                .body(TestConstants.OPERATION_RESULT, equalTo(String.valueOf(Result.ERROR)))
                .body(TestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(TestConstants.OPERATION_ERRORS_ERROR_CODE, equalTo("requestParameterInvalid"))
                .body(TestConstants.OPERATION_ERRORS_ERROR_MESSAGE,
                        equalTo("Resource request parameter is unable to be processed."))
                .body(TestConstants.OPERATION_ERRORS_MORE_INFO, equalTo(""))
                .body(TestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void seatCountTotalInvalidEventId() {
        // MethodArgumentTypeMismatchException. expected Long type but passed in value is String.
        RestAssured.given().port(port).when().pathParam(TestConstants.PATH_PARAM_EVENT_ID, "invalidEventId")
                .get(TestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(TestConstants.OPERATION_RESULT, equalTo(String.valueOf(Result.ERROR)))
                .body(TestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(TestConstants.OPERATION_ERRORS_ERROR_CODE, equalTo("pathParameterInvalid"))
                .body(TestConstants.OPERATION_ERRORS_ERROR_MESSAGE,
                        equalTo("Resource URI parameter is unable to be processed."))
                .body(TestConstants.OPERATION_ERRORS_MORE_INFO, equalTo(""))
                .body(TestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void seatCountTotalTypeAdultAndNotAisle() {
        requestParams.put(TestConstants.PARAM_IS_AISLE, false);
        requestParams.put(TestConstants.PARAM_SEAT_TYPE, TestConstants.SEAT_TYPE_ADULT);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 70);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 50);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalTypeAdultAndAisle() {
        requestParams.put(TestConstants.PARAM_IS_AISLE, true);
        requestParams.put(TestConstants.PARAM_SEAT_TYPE, TestConstants.SEAT_TYPE_ADULT);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 60);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalTypeChildAndNotAisle() {
        requestParams.put(TestConstants.PARAM_IS_AISLE, false);
        requestParams.put(TestConstants.PARAM_SEAT_TYPE, TestConstants.SEAT_TYPE_CHILD);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 30);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 60);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalTypeChildAndAisle() {
        requestParams.put(TestConstants.PARAM_IS_AISLE, true);
        requestParams.put(TestConstants.PARAM_SEAT_TYPE, TestConstants.SEAT_TYPE_CHILD);

        //run assertions
        apiAssertions(TestConstants.EVENT_ONE_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_TWO_ID, requestParams, 0);
        apiAssertions(TestConstants.EVENT_THREE_ID, requestParams, 30);
        apiAssertions(TestConstants.EVENT_FOUR_ID, requestParams, 0);
    }

    @Test
    public void seatCountTotalInvalidAcceptLanguage() {
        RestAssured.given().port(port).when().header("Accept-Language", "invalid header")
                .pathParam(TestConstants.PATH_PARAM_EVENT_ID, TestConstants.EVENT_ONE_ID)
                .get(TestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(TestConstants.OPERATION_RESULT, equalTo(String.valueOf(Result.ERROR)))
                .body(TestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(TestConstants.OPERATION_ERRORS_ERROR_CODE, equalTo("acceptLanguageHeaderInvalid"))
                .body(TestConstants.OPERATION_ERRORS_ERROR_MESSAGE,
                        equalTo("Request header Accept-Language is not in the expected format."))
                .body(TestConstants.OPERATION_ERRORS_MORE_INFO, equalTo(""))
                .body(TestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }

    @Test
    public void seatCountTotalMethodNotAllowed() {
        // Request method 'PATCH' not supported
        RestAssured.given().port(port).when().pathParam(TestConstants.PATH_PARAM_EVENT_ID, TestConstants.EVENT_ONE_ID)
                .patch(TestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_METHOD_NOT_ALLOWED);
    }

    /**
     * Runs assertions against the API for a given event id with optional query params
     *
     * @param eventId event id
     * @param requestParams map of query params
     * @param seatCount expected seat count
     */
    private void apiAssertions(Long eventId, Map<String, Object> requestParams, int seatCount) {
        RestAssured.given().port(port).when().pathParam(TestConstants.PATH_PARAM_EVENT_ID, eventId)
                .params(requestParams).when().get(TestConstants.COUNT_END_POINT).then().statusCode(HttpStatus.SC_OK)
                .body(TestConstants.SEAT_COUNT, equalTo(seatCount))
                .body(TestConstants.OPERATION_RESULT, equalTo(String.valueOf(Result.OK)))
                .body(TestConstants.OPERATION_CORRELATION_ID, notNullValue())
                .body(TestConstants.OPERATION_ERRORS_SIZE, equalTo(0))
                .body(TestConstants.OPERATION_REQUEST_TIMESTAMP, notNullValue());
    }
}
