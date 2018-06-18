package com.sample.api.event.seats.interceptor;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interceptor class for http request.
 */
public class RequestInterceptor extends HandlerInterceptorAdapter {

    /**
     * This implementation runs interceptor checks and always returns {@code true}
     *
     * @param request  the current incoming servlet request
     * @param response the current outgoing servlet response
     * @param handler  the handler (or {@link HandlerMethod})
     * @return {@code true}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        RequestInterceptorUtils.checkAcceptLanguageHeader(request);
        RequestInterceptorUtils.initRequestStartTime(request);
        return true;
    }
}
