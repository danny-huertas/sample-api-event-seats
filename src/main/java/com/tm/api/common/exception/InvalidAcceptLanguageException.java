package com.tm.api.common.exception;

/**
 * Exception to be thrown when a HTTP request asks, via 'Accept-Language' header, for response in an unsupported
 * language.
 */
public class InvalidAcceptLanguageException extends RuntimeException {
    private static final long serialVersionUID = 6235576245590546460L;

    public InvalidAcceptLanguageException(Throwable cause) {
        super(cause);
    }
}
