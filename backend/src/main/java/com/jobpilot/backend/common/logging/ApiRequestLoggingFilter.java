package com.jobpilot.backend.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ApiRequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ApiRequestLoggingFilter.class);

    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String requestId = getOrCreateRequestId(request);
        long startTime = System.currentTimeMillis();

        MDC.put("requestId", requestId);
        response.setHeader(REQUEST_ID_HEADER, requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            log.info(
                    "HTTP {} {} -> {} ({} ms)",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);

            MDC.remove("requestId");
        }
    }

    private String getOrCreateRequestId(HttpServletRequest request) {
        String requestId = request.getHeader(REQUEST_ID_HEADER);

        if (requestId == null || requestId.isBlank()) {
            return UUID.randomUUID().toString();
        }

        return requestId;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs");
    }
}