package com.altawfik.springbattleshipsapi.errorhandling.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class BaseException extends RuntimeException {

    private final HttpStatus httpStatus;

    private final Map<String, String> details;

    private final String origin;

    BaseException(final String message, final Throwable cause, final HttpStatus httpStatus, final Map<String, String> details, final String origin) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.details = details;
        this.origin = origin;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public String getOrigin() {
        return origin;
    }
}