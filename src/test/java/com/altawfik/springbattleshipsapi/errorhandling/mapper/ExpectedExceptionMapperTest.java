package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.exception.BaseException;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

class ExpectedExceptionMapperTest {

    private ExpectedExceptionMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ExpectedExceptionMapper();
    }

    @Test
    void shouldHandleContentExceptions() {
        String exceptionMessage = "Some message";
        String exceptionOrigin = "someOrigin";
        Throwable exceptionSource = new RuntimeException();
        HttpStatus exceptionHttpStatus = HttpStatus.BAD_REQUEST;
        HashMap<String, String> exceptionDetails = new HashMap<>();
        BaseException baseException = new ContentException(exceptionMessage, exceptionSource, exceptionHttpStatus,
                exceptionDetails, exceptionOrigin);

        BaseResponse baseResponse = mapper.map(baseException);

        assertThat(baseResponse.getError()).isNotNull();
        assertThat(baseResponse.getError().getMessage()).isEqualTo(exceptionMessage);
        assertThat(baseResponse.getError().getOrigin()).isEqualTo(exceptionOrigin);
        assertThat(baseResponse.getError().getHttpStatusCode()).isEqualTo(exceptionHttpStatus.value());
        assertThat(baseResponse.getError().getDetails()).isEqualTo(exceptionDetails);
    }
}