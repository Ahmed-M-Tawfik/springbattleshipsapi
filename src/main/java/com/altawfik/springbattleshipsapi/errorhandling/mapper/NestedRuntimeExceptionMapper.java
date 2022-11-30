package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.NestedRuntimeException;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class NestedRuntimeExceptionMapper {

    static final String API_VALIDATOR = "PAYLOAD_API_VALIDATOR";

    public BaseResponse map(final NestedRuntimeException e) {

        var mostSpecificCause = e.getMostSpecificCause();

        var detailMessage = mostSpecificCause.getMessage();

        if (mostSpecificCause instanceof JsonProcessingException) {
            detailMessage = ((JsonProcessingException) mostSpecificCause).getOriginalMessage();
        }

        var error = AppError.builder()
                            .httpStatusCode(BAD_REQUEST.value())
                            .message("Invalid payload")
                            .origin(API_VALIDATOR)
                            .details(Map.of("rootCause", detailMessage))
                            .build();

        return BaseResponse.builder()
                .error(error)
                .build();
    }
}
