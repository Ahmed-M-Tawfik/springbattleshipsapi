package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import org.springframework.stereotype.Component;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Component
public class UnexpectedExceptionMapper {

    private static final String UNEXPECTED_ORIGIN = "UNEXPECTED";

    public BaseResponse map(final Throwable throwable) {

        var error = AppError.builder()
                            .httpStatusCode(INTERNAL_SERVER_ERROR.value())
                            .message(throwable.getMessage())
                            .origin(UNEXPECTED_ORIGIN)
                            .build();

        return BaseResponse.builder().
                error(error).
                build();
    }
}
