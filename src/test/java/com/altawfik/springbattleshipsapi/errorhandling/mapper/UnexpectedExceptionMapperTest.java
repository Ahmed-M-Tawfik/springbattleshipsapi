package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class UnexpectedExceptionMapperTest {

    private UnexpectedExceptionMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new UnexpectedExceptionMapper();
    }

    @Test
    void shouldHandleContentExceptions() {
        String exceptionMessage = "Some message";
        Throwable exceptionSource = new RuntimeException();
        RuntimeException baseException = new RuntimeException(exceptionMessage, exceptionSource);

        BaseResponse baseResponse = mapper.map(baseException);

        HttpStatus exceptionHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        assertThat(baseResponse.getError()).isNotNull();
        assertThat(baseResponse.getError().getMessage()).isEqualTo(exceptionMessage);
        assertThat(baseResponse.getError().getOrigin()).isEqualTo("UNEXPECTED");
        assertThat(baseResponse.getError().getHttpStatusCode()).isEqualTo(exceptionHttpStatus.value());
        assertThat(baseResponse.getError().getDetails()).isNull();
    }
}