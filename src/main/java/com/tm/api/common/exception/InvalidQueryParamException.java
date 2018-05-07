package com.tm.api.common.exception;

public class InvalidQueryParamException extends RuntimeException {
    private static final long serialVersionUID = -6561409656922351453L;

    public InvalidQueryParamException(String parameterName) {
        super(parameterName);
    }

}
