package com.tm.api.event.seats.constants;

public class IntegrationTestConstants {

    public static final Long EVENT_ID = 1L;
    public static final String PATH_PARAM_EVENT_ID = "eventId";
    public static final String COUNT_END_POINT = "/{" + PATH_PARAM_EVENT_ID + "}/count";
    public static final String OPERATION_RESULT = "operation.result";
    public static final String OPERATION_CORRELATION_ID = "operation.correlationId";
    public static final String OPERATION_ERRORS_ERROR_CODE = "operation.errors[0].errorCode";
    public static final String OPERATION_ERRORS_ERROR_MESSAGE = "operation.errors[0].errorMessage";
    public static final String OPERATION_ERRORS_MORE_INFO = "operation.errors[0].moreInfo";
    public static final String OPERATION_REQUEST_TIMESTAMP = "operation.requestTimeStampUtc";

    private IntegrationTestConstants() {
        // Disallow instantiation
    }
}
