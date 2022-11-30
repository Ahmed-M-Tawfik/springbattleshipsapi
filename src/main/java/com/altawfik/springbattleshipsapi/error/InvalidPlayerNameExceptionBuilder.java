package com.altawfik.springbattleshipsapi.error;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ExceptionBuilder;
import org.springframework.http.HttpStatus;

public class InvalidPlayerNameExceptionBuilder extends ExceptionBuilder<InvalidPlayerNameExceptionBuilder> {

    public InvalidPlayerNameExceptionBuilder(final String playerName) {
        super(HttpStatus.BAD_REQUEST, "Invalid player name provided: " + playerName);
    }

    public InvalidPlayerNameException build() {
        return new InvalidPlayerNameException(message, cause, httpStatus, details, origin);
    }
}
