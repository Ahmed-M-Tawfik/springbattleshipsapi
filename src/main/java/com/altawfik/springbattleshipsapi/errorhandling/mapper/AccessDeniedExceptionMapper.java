package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import org.springframework.stereotype.Component;

import java.nio.file.AccessDeniedException;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@Component
public class AccessDeniedExceptionMapper {

    private static final String RESPONSE_HANDLER = "RESPONSE_HANDLER";

    public BaseResponse map(final AccessDeniedException exception) {

        var error = AppError.builder()
                            .httpStatusCode(FORBIDDEN.value())
                            .message(exception.getMessage())
                            .origin(RESPONSE_HANDLER)
                            .build();

        return BaseResponse.builder()
                .error(error)
                .build();
    }
}
