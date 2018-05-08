package com.tm.api.event.seats.exception;

import com.tm.api.common.error.ErrorConstants;
import com.tm.api.common.error.ErrorHelper;
import com.tm.api.common.exception.InvalidAcceptLanguageException;
import com.tm.api.common.exception.InvalidQueryParamException;
import com.tm.api.common.localization.MessageHandler;
import com.tm.api.common.operation.OperationError;
import org.hibernate.exception.GenericJDBCException;
import org.hibernate.exception.JDBCConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is for exception handling in the seats application
 */
@ControllerAdvice
public class ExceptionHandling {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandling.class);
    private static final String CRITICAL_ERROR = "criticalError";
    private static final String START_TIME = "startTime";
    private static final String EMPTY_FIELD_INFO = "";
    private MessageHandler messageHandler;
    private ErrorHelper errorHelper;
    private Tracer tracer;

    @Autowired
    public ExceptionHandling(MessageHandler messageHandler, ErrorHelper errorHelper, Tracer tracer) {
        this.messageHandler = messageHandler;
        this.errorHelper = errorHelper;
        this.tracer = tracer;
    }

    /**
     * Handles an unsupported media type exception.
     *
     * @param ex exception to be handled
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(value = { InvalidMediaTypeException.class, HttpMediaTypeNotSupportedException.class })
    public void handleUnsupportedMediaTypeException(Exception ex) {
        /*
         * This method doesn't return any response body, exception or error can
         * be identified by HTTP status code returned.
         */
        LOGGER.error("Unsupported or invalid media type exception occurred ", ex);
    }

    /**
     * Handles a bad request exception.
     *
     * @param ex exception to be handled
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MissingServletRequestParameterException.class, ServletRequestBindingException.class,
            MissingServletRequestPartException.class, BindException.class })
    public void handleBadRequestException(Exception ex) {
        /*
         * This method doesn't return any response body, exception or error can
         * be identified by HTTP status code returned.
         */
        LOGGER.error("Request parameter binding exception occurred ", ex);
    }

    /**
     * Handles a method not allowed exception.
     *
     * @param ex exception to be handled
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public void handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        /*
         * This method doesn't return any response body, exception or error can
         * be identified by HTTP status code returned.
         */
        LOGGER.error("Unsupported request method exception occurred ", ex);
    }

    /**
     * Handles an internal server exception.
     *
     * @param ex exception to be handled
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { MissingPathVariableException.class, ConversionNotSupportedException.class,
            HttpMessageNotWritableException.class })
    public void handleInternalServerErrorException(Exception ex) {
        /*
         * This method doesn't return any response body, exception or error can
         * be identified by HTTP status code returned.
         */
        LOGGER.error("Other exception occurred ", ex);
    }

    /**
     * Handles an exception that is thrown when the requested url mapping is not found.
     *
     * @param ex exception to be handled
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = { NoHandlerFoundException.class })
    public void handleNotFoundException(Exception ex) {
        /*
         * This method doesn't return any response body, exception or error can
         * be identified by HTTP status code returned.
         */
        LOGGER.error("Handler exception occurred ", ex);
    }

    /**
     * Handles an exception that is thrown when the media type is not acceptable
     *
     * @param ex exception to be handled
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public void handleMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        /*
         * This method doesn't return any response body, exception or error can
         * be identified by HTTP status code returned.
         */
        LOGGER.error("Media type not acceptable exception occurred ", ex);
    }

    /**
     * Handles a global exception
     *
     * @param request original request received
     * @param ex exception to be handled
     * @return error response body.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public OperationError handleGlobalException(final HttpServletRequest request, final Exception ex) {
        /*
         * For unhandled exceptions, log message would contain the String criticalError so that notifications/alerts can be
         * sent by parsing the String criticalError. Example log message would look like the below message.
         * LogMessage=Unhandled exception occurred, is criticalError and exception class java.lang.NullPointerException
         */
        LOGGER.error("Unhandled exception occurred, is {} and exception {} ", CRITICAL_ERROR, ex.getClass(), ex);
        return errorHelper
                .errorResponse(ErrorConstants.ERROR_CODE_INTERNAL_ERROR, tracer.getCurrentSpan().traceIdString(),
                        messageHandler.localizeMessage(ErrorConstants.ERROR_CODE_INTERNAL_ERROR), EMPTY_FIELD_INFO,
                        request.getAttribute(START_TIME));
    }

    /**
     * Handles a sql exception
     *
     * @param request original request received
     * @param ex exception to be handled
     * @return error response body.
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = { CannotCreateTransactionException.class, SQLException.class, PersistenceException.class,
            GenericJDBCException.class, InvalidDataAccessResourceUsageException.class,
            DataAccessResourceFailureException.class, JDBCConnectionException.class })
    @ResponseBody
    public OperationError handleSQLException(final HttpServletRequest request, final Exception ex) {
        /*
         * For database exceptions, log message would contain the String criticalError so that notifications/alerts can be
         * sent by parsing the String criticalError. Example log message would look like the below message.
         * LogMessage=Unhandled exception occurred, is criticalError and exception class java.sql.SQLException
         */
        LOGGER.error("Database exception occurred, is {} and exception {} ", CRITICAL_ERROR, ex.getClass(), ex);
        return errorHelper
                .errorResponse(ErrorConstants.ERROR_CODE_INTERNAL_ERROR, tracer.getCurrentSpan().traceIdString(),
                        messageHandler.localizeMessage(ErrorConstants.ERROR_CODE_INTERNAL_ERROR), EMPTY_FIELD_INFO,
                        request.getAttribute(START_TIME));
    }

    /**
     * Handles an invalid data format exception
     *
     * @param request original request received
     * @param ex exception to be handled
     * @return error response body.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class, TypeMismatchException.class })
    @ResponseBody
    public OperationError handleInvalidDataFormatException(final HttpServletRequest request, final Exception ex) {
        LOGGER.error("MethodArgumentTypeMismatchException occurred ", ex);
        return errorHelper
                .errorResponse(ErrorConstants.ERROR_CODE_INVALID_PATH_PARAM, tracer.getCurrentSpan().traceIdString(),
                        messageHandler.localizeMessage(ErrorConstants.ERROR_CODE_INVALID_PATH_PARAM), EMPTY_FIELD_INFO,
                        request.getAttribute(START_TIME));
    }

    /**
     * Handles an invalid data format exception
     *
     * @param request original request received
     * @param ex exception to be handled
     * @return error response body.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = { IllegalStateException.class })
    @ResponseBody
    public OperationError handleInvalidDataValueException(final HttpServletRequest request, final Exception ex) {
        LOGGER.error("IllegalStateException occurred ", ex);
        return errorHelper
                .errorResponse(ErrorConstants.ERROR_CODE_INVALID_REQUEST_PARAM, tracer.getCurrentSpan().traceIdString(),
                        messageHandler.localizeMessage(ErrorConstants.ERROR_CODE_INVALID_REQUEST_PARAM),
                        EMPTY_FIELD_INFO, request.getAttribute(START_TIME));
    }

    /**
     * Handles an invalid accept language exception
     *
     * @param request original request received
     * @return error response body.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = InvalidAcceptLanguageException.class)
    @ResponseBody
    public OperationError handleInvalidAcceptLanguageException(final HttpServletRequest request,
            final InvalidAcceptLanguageException ex) {
        /*
         * errorMessage for this method is hard-coded since this is thrown in RequestInterceptor and the
         * messages.properties files will not be loaded to provide a meaningful response back.
         */
        LOGGER.error("Invalid Accept-Language exception occurred ", ex);
        return errorHelper.errorResponse(ErrorConstants.ERROR_CODE_ACCEPT_LANGUAGE_HEADER_INVALID,
                tracer.getCurrentSpan().traceIdString(),
                "Request header Accept-Language is not in the expected format.", EMPTY_FIELD_INFO,
                request.getAttribute(START_TIME));
    }

    /**
     * Handles a data type mismatch exception
     *
     * @param request original request received
     * @return error response body.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public OperationError handleDataTypeMismatchException(final HttpServletRequest request,
            final HttpMessageNotReadableException ex) {
        LOGGER.error("Message not readable exception occurred ", ex);
        return errorHelper
                .errorResponse(ErrorConstants.ERROR_CODE_REQUEST_BODY_INVALID, tracer.getCurrentSpan().traceIdString(),
                        messageHandler.localizeMessage(ErrorConstants.ERROR_CODE_REQUEST_BODY_INVALID),
                        EMPTY_FIELD_INFO, request.getAttribute(START_TIME));
    }

    /**
     * Used to send the Error Response for the Body Validation validated using @Valid annotation.
     *
     * @param ex exception to be handled
     * @return error response body.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public OperationError handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        return handleMethodArgumentNotValidException(tracer.getCurrentSpan().traceIdString(), ex);
    }

    /**
     * Handles a date time param not valid exception
     *
     * @return error response body.
     */
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidQueryParamException.class)
    @ResponseBody
    public OperationError handleQueryParamNotValidException(final InvalidQueryParamException ex) {
        LOGGER.error("Error occurred while parsing query parameters.", ex);
        return errorHelper
                .errorResponse(ErrorConstants.ERROR_QUERY_PARAMETER_INVALID, tracer.getCurrentSpan().traceIdString(),
                        messageHandler.localizeMessage(ErrorConstants.ERROR_QUERY_PARAMETER_INVALID), ex.getMessage(),
                        Instant.ofEpochMilli(tracer.getCurrentSpan().getBegin()));
    }

    /**
     * Handler for Bean Validation Framework exceptions.
     *
     * @param traceId traceId of the request
     * @param ex exception that occurred during processing the request
     * @return error response body
     */
    private OperationError handleMethodArgumentNotValidException(String traceId,
            final MethodArgumentNotValidException ex) {
        final List<String> validationErrors = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        /*
         * Note: There is no exception Logging here, since when this exception
         * occurs as the stack trace gets printed, it also prints the sensitive
         * information in some cases. The error code is enough in resolving the
         * issue in all the cases, so no need to Log the exception.
         */
        return errorHelper.errorResponse(validationErrors, traceId);
    }
}
