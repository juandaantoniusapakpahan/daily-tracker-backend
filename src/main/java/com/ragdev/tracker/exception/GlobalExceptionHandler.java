package com.ragdev.tracker.exception;

import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.enums.ApiCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResApiDto<Object, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField()+": "+err.getDefaultMessage())
                .collect(Collectors.toList());

        ResApiDto<Object, Object> response = new ResApiDto<>(
                "error",
                ApiCode.VALIDATION_FAILED.getCode(),
                ApiCode.VALIDATION_FAILED.getMessage(),
                LocalDateTime.now(),
                null,
                errors
                );
        return ResponseEntity.status(Integer.parseInt(ApiCode.VALIDATION_FAILED.getCode())).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResApiDto<Object, Object>> handleResourceNotFound(ResourceNotFoundException ex) {

        ResApiDto<Object, Object> response = new ResApiDto<>(
                "error",
                ApiCode.NOT_FOUND.getCode(),
                ApiCode.NOT_FOUND.getMessage(),
                LocalDateTime.now(),
                null,
                ex.getMessage()
        );

        return ResponseEntity.status(Integer.parseInt(ApiCode.VALIDATION_FAILED.getCode())).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResApiDto<Object, Object>> handleBadRequest(BadRequestException ex) {

        ResApiDto<Object,Object> response = new ResApiDto<>(
                "error",
                ApiCode.BAD_REQUEST.getCode(),
                ApiCode.BAD_REQUEST.getMessage(),
                LocalDateTime.now(),
                null,
                ex.getMessage()
        );

        return ResponseEntity.status(Integer.parseInt(ApiCode.BAD_REQUEST.getCode())).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResApiDto<Object, Object>> handleRuntime(RuntimeException ex) {

        ResApiDto<Object,Object> response = new ResApiDto<>(
                "error",
                ApiCode.BAD_REQUEST.getCode(),
                ApiCode.BAD_REQUEST.getMessage(),
                LocalDateTime.now(),
                null,
                ex.getMessage()
        );
        return ResponseEntity.status(Integer.parseInt(ApiCode.BAD_REQUEST.getCode())).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResApiDto<Object, Object>> handleException(Exception ex) {
        ResApiDto<Object,Object> response = new ResApiDto<>(
                "error",
                ApiCode.INTERNAL_ERROR.getCode(),
                ApiCode.INTERNAL_ERROR.getMessage(),
                LocalDateTime.now(),
                null,
                List.of(ex.getMessage())
        );

        log.error("Internal Error: {}", ex.getMessage());
        return ResponseEntity.status(Integer.parseInt(ApiCode.INTERNAL_ERROR.getCode())).body(response);
    }
}