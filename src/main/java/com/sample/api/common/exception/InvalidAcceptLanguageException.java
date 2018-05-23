package com.sample.api.common.exception;

/**
 * This Exception is thrown when a HTTP request asks, via 'Accept-Language' header, for response in an unsupported language.
 */
public class InvalidAcceptLanguageException extends RuntimeException {
    public InvalidAcceptLanguageException(Throwable cause) {
        super(cause);
    }
}
