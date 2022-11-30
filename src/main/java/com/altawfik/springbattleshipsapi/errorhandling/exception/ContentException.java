package com.altawfik.springbattleshipsapi.errorhandling.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class ContentException extends BaseException {

    public ContentException(final String message, final Throwable cause, final HttpStatus httpStatus, final Map<String, String> details, final String origin) {
        super(message, cause, httpStatus, details, origin);
    }
}