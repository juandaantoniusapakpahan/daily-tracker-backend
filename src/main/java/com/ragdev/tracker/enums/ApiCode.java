package com.ragdev.tracker.enums;

public enum ApiCode {

    SUCCESS("200", "Operation successful"),

    // Client Errors
    BAD_REQUEST("400", "Bad request"),
    VALIDATION_FAILED("400", "Validation failed"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Resource not found"),

    // Server Errors
    INTERNAL_ERROR("500", "Internal server error"),
    SERVICE_UNAVAILABLE("503", "Service unavailable");

    private final String code;
    private final String message;

    ApiCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}
