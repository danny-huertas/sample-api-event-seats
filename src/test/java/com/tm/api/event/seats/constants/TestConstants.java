package com.tm.api.event.seats.constants;

/**
 * This cass is used to store constants used by integration tests
 */
public class TestConstants {
    public static final Long EVENT_ONE_ID = 1L;
    public static final Long EVENT_TWO_ID = 2L;
    public static final Long EVENT_THREE_ID = 3L;
    public static final Long EVENT_FOUR_ID = 4L;
    public static final String SEAT_TYPE_CHILD = "child";
    public static final String SEAT_TYPE_ADULT = "adult";
    public static final String PARAM_SEAT_TYPE = "type";
    public static final String PARAM_IS_AISLE = "aisle";
    public static final String PARAM_IS_AVAILABLE = "available";
    public static final String PATH_PARAM_EVENT_ID = "eventId";
    public static final String COUNT_END_POINT = "/{" + PATH_PARAM_EVENT_ID + "}/count";
    public static final String SEAT_COUNT = "seatCount";
    public static final String OPERATION_RESULT = "operation.result";
    public static final String OPERATION_CORRELATION_ID = "operation.correlationId";
    public static final String OPERATION_ERRORS_SIZE = "operation.errors.size()";
    public static final String OPERATION_ERRORS_ERROR_CODE = "operation.errors[0].errorCode";
    public static final String OPERATION_ERRORS_ERROR_MESSAGE = "operation.errors[0].errorMessage";
    public static final String OPERATION_ERRORS_MORE_INFO = "operation.errors[0].moreInfo";
    public static final String OPERATION_REQUEST_TIMESTAMP = "operation.requestTimeStampUtc";

    private TestConstants() {
        // Disallow instantiation
    }
}
