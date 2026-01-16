package com.ragdev.tracker.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class ApiLoggingFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest =
                new ContentCachingRequestWrapper(request);

        ContentCachingResponseWrapper wrappedResponse =
                new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {

            long duration = System.currentTimeMillis() - startTime;

            logRequest(wrappedRequest);
            logResponse(wrappedResponse, duration);

            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String body = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("REQUEST: {} | {} | body={}",
                request.getMethod(),
                request.getRequestURI(),
                body.isBlank() ? "-" : body);
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration)
            throws IOException {

        String body = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("RESPONSE: status={} | duration={}ms | body={}",
                response.getStatus(),
                duration,
                body.isBlank() ? "-" : body);
    }
}