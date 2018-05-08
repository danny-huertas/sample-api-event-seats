package com.tm.api.event.seats.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * This class is used to produce consistent log output.
 */
@Aspect
@Component
public class LogAspect extends LogAspectBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Around("execution(* com.tm.api.event.seats.controllers.*.*(..))")
    public Object doAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        // for Controller the RequestFacade argument is removed from log
        Object[] args = Arrays.stream(joinPoint.getArgs()).map(this::rewriteArgument).toArray();
        return doAroundDebug(joinPoint, args);
    }

    @Around("execution(* com.tm.api.event.seats.service.*.*(..))")
    public Object doAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        return doAroundDebug(joinPoint, joinPoint.getArgs());
    }

    @Around("execution(* com.tm.api.event.seats.resource.dao.*.*(..))")
    public Object doAroundResourceHelper(ProceedingJoinPoint joinPoint) throws Throwable {
        return doAroundDebug(joinPoint, joinPoint.getArgs());
    }

    @Around("execution(* com.tm.api.event.seats.exception.*.*(..)) ")
    public Object doAroundException(ProceedingJoinPoint joinPoint) throws Throwable {
        return doAroundError(joinPoint, joinPoint.getArgs());
    }
}
