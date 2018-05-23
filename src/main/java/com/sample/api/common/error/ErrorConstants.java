package com.sample.api.common.error;

/**
 * Constant properties associated to errors
 */
public class ErrorConstants {
    // Error codes
    public static final String ERROR_CODE_INTERNAL_ERROR = "internalError";
    public static final String ERROR_CODE_INVALID_PATH_PARAM = "pathParameterInvalid";
    public static final String ERROR_CODE_INVALID_REQUEST_PARAM = "requestParameterInvalid";
    public static final String ERROR_CODE_REQUEST_BODY_INVALID = "requestBodyInvalid";
    public static final String ERROR_QUERY_PARAMETER_INVALID = "queryParameterInvalid";
    public static final String ERROR_CODE_ACCEPT_LANGUAGE_HEADER_INVALID = "acceptLanguageHeaderInvalid";
    static final String ERROR_FORMAT_NOT_SUPPORTED = "errorFormatNotSupported";
    static final String ERROR_SPLITTER = "-";

    private ErrorConstants() {
        // Disallow instantiation
    }
}
