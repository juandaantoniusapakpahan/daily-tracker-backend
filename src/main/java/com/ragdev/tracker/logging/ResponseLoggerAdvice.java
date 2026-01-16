package com.ragdev.tracker.logging;

import com.ragdev.tracker.dto.ResApiDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
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
        if (body instanceof ResApiDto<?,?> resApiDto) {
            if ("success".equalsIgnoreCase(resApiDto.getStatus())) {
                log.info("SUCCESS API {} {} -> {}",
                        request.getMethod(),
                        request.getURI(),
                        resApiDto.getData()); // still need to fix this
            } else if ("error".equalsIgnoreCase(resApiDto.getStatus())) {
                log.error("ERROR API {} {} -> {}",
                        request.getMethod(),
                        request.getURI(),
                        resApiDto.getErrors()); // still need to fix this
            }
        } else {
            log.info("API {} {} -> {}", request.getMethod(), request.getURI(), body);
        }

        return body;
    }
}
