package com.altawfik.springbattleshipsapi.error;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import org.springframework.http.HttpStatus;

import java.util.Map;

public class InvalidBoardPositionException extends ContentException {
    public InvalidBoardPositionException(final String message, final Throwable cause, final HttpStatus httpStatus, final Map<String, String> details, final String origin) {
        super(message, cause, httpStatus, details, origin);
    }
}
