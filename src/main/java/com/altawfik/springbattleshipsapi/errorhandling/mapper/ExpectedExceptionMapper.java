package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import com.altawfik.springbattleshipsapi.errorhandling.exception.BaseException;
import org.springframework.stereotype.Component;

@Component
public class ExpectedExceptionMapper {

    public BaseResponse map(final BaseException exception) {

        var error = AppError.builder()
                            .httpStatusCode(exception.getHttpStatus().value())
                            .message(exception.getMessage())
                            .origin(exception.getOrigin())
                            .details(exception.getDetails())
                            .build();

        return BaseResponse.builder()
                .error(error)
                .build();
    }
}
