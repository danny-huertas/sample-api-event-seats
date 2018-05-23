package com.sample.api.event.seats.interceptor;

import com.sample.api.common.exception.InvalidAcceptLanguageException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Locale;

/**
 * This class defines  helper methods that can be used in the application request interceptors to achieve the
 * desired business logic. Every request interceptor implementation is expected to define its own business logic
 * behaviour, e.g. within the preHandle method. The implementations of the business logic methods is expected to make
 * use of the helper methods from this class.
 */
class RequestInterceptorUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptorUtils.class);
    private static final String START_TIME = "startTime";

    private RequestInterceptorUtils() {
        // Disallow instantiation.
    }

    /**
     * Checks the "Accept-Language" header in the current request. If the request contains the header, then it must
     * contain a valid value, otherwise the {code InvalidAcceptLanguageException} is thrown.
     *
     * @param request the current incoming HTTP request
     * @throws InvalidAcceptLanguageException thrown if the current request specifies the "accept-language" header with
     *         an incorrect value.
     */
    static void checkAcceptLanguageHeader(HttpServletRequest request) {
        String acceptLangHeader = request.getHeader(HttpHeaders.ACCEPT_LANGUAGE);
        if (StringUtils.isNotBlank(acceptLangHeader)) {
            // if the "accept-language" is sent, then it must be valid.
            try {
                Locale.LanguageRange.parse(acceptLangHeader);
            } catch (IllegalArgumentException | NullPointerException ex) {
                LOGGER.error("Invalid Accept-Language header: ", ex);
                throw new InvalidAcceptLanguageException(ex);
            }
        }
    }

    /**
     * Checks whether the request contains timestamp object that represents the
     * start of the request processing and initializes it if needed.
     *
     * @param request the current incoming servlet request
     */
    static void initRequestStartTime(ServletRequest request) {
        if (request.getAttribute(START_TIME) == null) {
            request.setAttribute(START_TIME, Instant.now().toString());
        }
    }
}
