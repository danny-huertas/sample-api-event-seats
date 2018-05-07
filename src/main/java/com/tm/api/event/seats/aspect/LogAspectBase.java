package com.tm.api.event.seats.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.catalina.connector.RequestFacade;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * Base class for LogAspectBase classes in individual projects in TM_API. Once overridden: 1. implement
 * {@link #getLogger()} to provide the {@code Logger} instance 2. define the advices as needed and use
 * {@link #doAround(ProceedingJoinPoint)} or {@link #doAround(ProceedingJoinPoint, Object[])} to produce consistent log
 * output.
 */
public abstract class LogAspectBase {
    private static final String LOG_MESSAGE = "(*LOG*)";
    private static final String BEFORE_RUNNING_CLASS = " Before running, Interface/Class intercepted is :  ";
    private static final String BEFORE_RUNNING_METHOD = " Before running, Method intercepted is : ";
    private static final String METHOD_ARGS = " Arguments passed to method : ";
    private static final String AFTER_RUNNING_CLASS = " After running, Interface/Class is :  ";
    private static final String AFTER_RUNNING_METHOD = " After running, Method is : ";
    private static final String AFTER_RETURN_VALUE = " After returning, the return value is : ";

    /**
     * Implementation class must provide the logger
     *
     * @return Logger instance to be used for logging
     */
    protected abstract Logger getLogger();

    /**
     * Produces log info about the class, method, arguments and return value, before and after proceeding with the next
     * advice or target method invocation, in the consistent format. For cases when the arguments need to be manipulated
     * somehow, use {@link #doAround(ProceedingJoinPoint, Object[])}.
     *
     * @param joinPoint keeps the info about class, method and parameters and is used to proceed with the target
     * @return {@link Object} returned from invoking {@link ProceedingJoinPoint#proceed()}
     * @throws Throwable from {@link ProceedingJoinPoint#proceed()}
     */
    Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return doAround(joinPoint, joinPoint.getArgs());
    }

    /**
     * Produces log info for cases when the arguments need to be manipulated somehow. The caller obtains the arguments
     * via {@link ProceedingJoinPoint#getArgs()}, changes them as needed and passes them as <code>args</code>
     *
     * @param joinPoint keeps the info about class, method and parameters and is used to proceed with the target
     * @param args array of arguments to be logged
     * @return {@link Object} returned from invoking {@link ProceedingJoinPoint#proceed()}
     * @throws Throwable from {@link ProceedingJoinPoint#proceed()}
     */
    Object doAround(ProceedingJoinPoint joinPoint, @NotNull Object[] args) throws Throwable {
        final ObjectMapper om = new ObjectMapper();

        // log before invoking the target method
        if (getLogger().isTraceEnabled()) {
            getLogger().trace(LOG_MESSAGE + BEFORE_RUNNING_CLASS + joinPoint.getSignature().getDeclaringTypeName());
            getLogger().trace(LOG_MESSAGE + BEFORE_RUNNING_METHOD + joinPoint.getSignature().getName());
            getLogger().trace(LOG_MESSAGE + METHOD_ARGS + om.writerWithDefaultPrettyPrinter().writeValueAsString(args));
        }

        /*
         * Note: This method can throw any Throwable and if this happens then nothing is logged from this layer.
         */
        final Object value = joinPoint.proceed();

        // log after invocation
        if (getLogger().isTraceEnabled()) {
            getLogger().trace(LOG_MESSAGE + AFTER_RUNNING_CLASS + joinPoint.getSignature().getDeclaringTypeName());
            getLogger().trace(LOG_MESSAGE + AFTER_RUNNING_METHOD + joinPoint.getSignature().getName());
            getLogger().trace(LOG_MESSAGE + AFTER_RETURN_VALUE + om.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(value));
        }
        return value;
    }

    /**
     * Produces log info for cases when the arguments need to be manipulated somehow. The caller obtains the arguments
     * via {@link ProceedingJoinPoint#getArgs()}, changes them as needed and passes them as <code>args</code>.
     *
     * @param joinPoint keeps the info about class, method and parameters and is used to proceed with the target
     * @param args array of arguments to be logged
     * @return {@link Object} returned from invoking {@link ProceedingJoinPoint#proceed()}
     * @throws Throwable from {@link ProceedingJoinPoint#proceed()}
     */
    Object doAroundDebug(ProceedingJoinPoint joinPoint, @NotNull Object[] args) throws Throwable {

        // log before invoking the target method
        if (getLogger().isDebugEnabled()) {
            getLogger()
                    .debug(LOG_MESSAGE + BEFORE_RUNNING_CLASS + joinPoint.getSignature().getDeclaringTypeName() + ". "
                            + BEFORE_RUNNING_METHOD + joinPoint.getSignature().getName() + ". " + METHOD_ARGS + Arrays
                            .toString(args));
        }

        /*
         * Note: This method can throw any Throwable and if this happens then nothing is logged from this layer.
         */
        final Object value = joinPoint.proceed();

        // log after invocation
        if (getLogger().isDebugEnabled()) {
            getLogger().debug(LOG_MESSAGE + AFTER_RUNNING_CLASS + joinPoint.getSignature().getDeclaringTypeName() + ". "
                    + AFTER_RUNNING_METHOD + joinPoint.getSignature().getName());
        }
        return value;
    }

    /**
     * Produces log info for cases when the arguments need to be manipulated somehow. The caller obtains the arguments
     * via {@link ProceedingJoinPoint#getArgs()}, changes them as needed and passes them as <code>args</code>
     *
     * @param joinPoint keeps the info about class, method and parameters and is used to proceed with the target
     * @param args array of arguments to be logged
     * @return {@link Object} returned from invoking {@link ProceedingJoinPoint#proceed()}
     * @throws Throwable from {@link ProceedingJoinPoint#proceed()}
     */
    Object doAroundError(ProceedingJoinPoint joinPoint, @NotNull Object[] args) throws Throwable {
        final ObjectMapper om = new ObjectMapper();

        // log before invoking the target method
        if (getLogger().isErrorEnabled()) {
            getLogger()
                    .error(LOG_MESSAGE + BEFORE_RUNNING_CLASS + joinPoint.getSignature().getDeclaringTypeName() + ". "
                            + BEFORE_RUNNING_METHOD + joinPoint.getSignature().getName() + ". " + METHOD_ARGS + Arrays
                            .toString(args));
        }

        /*
         * Note: This method can throw any Throwable and if this happens then nothing is logged from this layer.
         */
        final Object value = joinPoint.proceed();

        // log after invocation
        if (getLogger().isErrorEnabled()) {
            getLogger().error(LOG_MESSAGE + AFTER_RUNNING_CLASS + joinPoint.getSignature().getDeclaringTypeName() + ". "
                    + AFTER_RUNNING_METHOD + joinPoint.getSignature().getName() + ". " + AFTER_RETURN_VALUE + om
                    .writerWithDefaultPrettyPrinter().writeValueAsString(value));
        }
        return value;
    }

    /**
     * Rewrites the request argument if it is of the given class
     *
     * @param arg the argument to be rewritten, can be null
     * @return the given argument or the rewritten argument or null if null was passed as {@code} arg {@code}
     */
    Object rewriteArgument(Object arg) {
        if ((arg != null) && RequestFacade.class.getName().equalsIgnoreCase(arg.getClass().getName())) {
            return "Argument was removed.";
        }
        return arg;
    }
}