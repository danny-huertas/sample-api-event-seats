package com.tm.api.common.exception;

/**
 * This Exception is thrown when a query has an invalid param
 */
public class InvalidQueryParamException extends RuntimeException {
    public InvalidQueryParamException(String parameterName) {
        super(parameterName);
    }
}
