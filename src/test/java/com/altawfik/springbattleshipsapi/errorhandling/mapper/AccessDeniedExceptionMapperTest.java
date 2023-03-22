package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.exception.BaseException;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AccessDeniedExceptionMapperTest {
    private AccessDeniedExceptionMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new AccessDeniedExceptionMapper();
    }

    @Test
    void shouldHandleAccessDeniedException() {
        String exceptionMessage = "Some message";
        String exceptionOrigin = "RESPONSE_HANDLER";
        HttpStatus exceptionHttpStatus = HttpStatus.FORBIDDEN;
        AccessDeniedException exception = new AccessDeniedException(exceptionMessage);

        BaseResponse baseResponse = mapper.map(exception);

        assertThat(baseResponse.getError()).isNotNull();
        assertThat(baseResponse.getError().getMessage()).isEqualTo(exceptionMessage);
        assertThat(baseResponse.getError().getOrigin()).isEqualTo(exceptionOrigin);
        assertThat(baseResponse.getError().getHttpStatusCode()).isEqualTo(exceptionHttpStatus.value());
    }
}