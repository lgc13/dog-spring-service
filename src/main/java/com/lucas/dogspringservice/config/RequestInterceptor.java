package com.lucas.dogspringservice.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Value("${gateway.url}")
    private String gatewayUrl;

    @Override
    public boolean preHandle(
        HttpServletRequest request,
        HttpServletResponse response,
        Object handler
    ) {
        String xForwardedHostHeader = request.getHeader("x-forwarded-host");
        String xForwardedProtoHeader = request.getHeader("x-forwarded-proto");
        String fullUrlHeader = xForwardedProtoHeader + "://" + xForwardedHostHeader;

        log.info("gatewayUrl: {}", gatewayUrl);
        if (fullUrlHeader.equals(gatewayUrl)) {
            return true;
        } else {
            log.info("Unauthorized request from {}", fullUrlHeader);
            response.setStatus(401);
            return false;
        }
    }
}
