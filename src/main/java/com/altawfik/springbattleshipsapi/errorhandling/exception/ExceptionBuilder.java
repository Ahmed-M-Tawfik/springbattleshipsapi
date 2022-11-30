package com.altawfik.springbattleshipsapi.errorhandling.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public abstract class ExceptionBuilder<T extends ExceptionBuilder<T>> {

    protected final HttpStatus httpStatus;

    protected final String message;

    protected Map<String, String> details;

    protected Throwable cause;

    protected String origin;

    public ExceptionBuilder(final HttpStatus httpStatus, final String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public ExceptionBuilder<T> withDetails(final Map<String, String> details) {
        this.details = details;
        return this;
    }

    public ExceptionBuilder<T> withCause(final Throwable cause) {
        this.cause = cause;
        return this;
    }

    public ExceptionBuilder<T> withOrigin(final String origin) {
        this.origin = origin;
        return this;
    }

    public abstract BaseException build();
}
