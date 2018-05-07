package com.tm.api.event.seats.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor class for http request.
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestInterceptorUtils.checkAcceptLanguageHeader(request);
        RequestInterceptorUtils.initRequestStartTime(request);
        return true;
    }
}
