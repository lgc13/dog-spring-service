package com.lucas.dogspringservice.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Value("${GATEWAY_URL}")
    private String GATEWAY_URL;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        String xForwardedHostHeader = request.getHeader("x-forwarded-host");
        String xForwardedProtoHeader = request.getHeader("x-forwarded-proto");
        String fullUrlHeader = xForwardedProtoHeader + "://" + xForwardedHostHeader;


        if (fullUrlHeader.equals(GATEWAY_URL)) {
            return true;
        } else {
            log.info("Unauthorized request from {}", fullUrlHeader);
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void postHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        ModelAndView modelAndView
    )
        throws Exception {
        log.info(" --- in postHandle ---");
    }

    @Override
    public void afterCompletion(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler,
        Exception ex
    )
        throws Exception {
        log.info("--- in afterCompletion ---");
    }
}
