package com.ragdev.tracker.exception;

import com.ragdev.tracker.dto.ApiResponse;
import com.ragdev.tracker.enums.ApiCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object, Object>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField()+": "+err.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponse<Object, Object> response = new ApiResponse<> (
                "error",
                ApiCode.VALIDATION_FAILED.getCode(),
                ApiCode.VALIDATION_FAILED.getMessage(),
                null,
                errors
                );
        return ResponseEntity.status(Integer.parseInt(ApiCode.VALIDATION_FAILED.getCode())).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object, Object>> handleResourceNotFound(ResourceNotFoundException ex) {

        ApiResponse<Object, Object> response = new ApiResponse<> (
                "error",
                ApiCode.NOT_FOUND.getCode(),
                ApiCode.NOT_FOUND.getMessage(),
                null,
                ex.getMessage()
        );

        return ResponseEntity.status(Integer.parseInt(ApiCode.VALIDATION_FAILED.getCode())).body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object, Object>> handleBadRequest(BadRequestException ex) {

        ApiResponse<Object,Object> response = new ApiResponse<>(
                "error",
                ApiCode.BAD_REQUEST.getCode(),
                ApiCode.BAD_REQUEST.getMessage(),
                null,
                ex.getMessage()
        );

        return ResponseEntity.status(Integer.parseInt(ApiCode.BAD_REQUEST.getCode())).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object, Object>> handleRuntime(RuntimeException ex) {

        ApiResponse<Object,Object> response = new ApiResponse<>(
                "error",
                ApiCode.BAD_REQUEST.getCode(),
                ApiCode.BAD_REQUEST.getMessage(),
                null,
                ex.getMessage()
        );

        return ResponseEntity.status(Integer.parseInt(ApiCode.BAD_REQUEST.getCode())).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object, Object>> handleException(Exception ex) {

        ApiResponse<Object,Object> response = new ApiResponse<>(
                "error",
                ApiCode.INTERNAL_ERROR.getCode(),
                ApiCode.INTERNAL_ERROR.getMessage(),
                null,
                List.of(ex.getMessage())
        );

        return ResponseEntity.status(Integer.parseInt(ApiCode.INTERNAL_ERROR.getCode())).body(response);
    }
}