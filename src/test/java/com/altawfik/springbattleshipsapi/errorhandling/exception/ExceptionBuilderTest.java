package com.altawfik.springbattleshipsapi.errorhandling.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ExceptionBuilderTest {

    @Test
    public void testCreation() {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = "Some error message";
        var cause = new RuntimeException();
        String origin = "Some origin";
        Map<String, String> details = Map.of("someKey", "someValue");

        var exception = new ContentExceptionBuilder(httpStatus, message)
                .withCause(cause)
                .withOrigin(origin)
                .withDetails(details)
                .build();

        assertAll("Should match source data",
                () -> assertThat(exception.getHttpStatus()).isEqualTo(httpStatus),
                () -> assertThat(exception.getMessage()).isEqualTo(message),
                () -> assertThat(exception.getCause()).isEqualTo(cause),
                () -> assertThat(exception.getOrigin()).isEqualTo(origin),
                () -> assertThat(exception.getDetails()).isEqualTo(details)
        );
    }

}