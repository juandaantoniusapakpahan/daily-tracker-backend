package com.ragdev.tracker.logging;

import com.ragdev.tracker.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@ControllerAdvice
public class ResponseLoggerAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body instanceof ApiResponse<?,?> apiResponse) {
            if ("success".equalsIgnoreCase(apiResponse.getStatus())) {
                log.info("SUCCESS API {} {} -> {}",
                        request.getMethod(),
                        request.getURI(),
                        apiResponse.getData());
            } else if ("error".equalsIgnoreCase(apiResponse.getStatus())) {
                log.error("ERROR API {} {} -> {}",
                        request.getMethod(),
                        request.getURI(),
                        apiResponse.getErrors());
            }
        } else {
            log.info("API {} {} -> {}", request.getMethod(), request.getURI(), body);
        }

        return body;
    }
}
