package com.altawfik.springbattleshipsapi.errorhandling.exception;

import org.springframework.http.HttpStatus;

public class ContentExceptionBuilder extends ExceptionBuilder<ContentExceptionBuilder> {

    public ContentExceptionBuilder(final String message) {
        this(HttpStatus.BAD_REQUEST, message);
    }

    public ContentExceptionBuilder(final HttpStatus httpStatus, final String message) {
        super(httpStatus, message);
    }

    public ContentException build() {
        return new ContentException(message, cause, httpStatus, details, origin);
    }
}
