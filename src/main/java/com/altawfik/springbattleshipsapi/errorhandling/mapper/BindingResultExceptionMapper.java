package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class BindingResultExceptionMapper {

    static final String API_VALIDATOR = "PAYLOAD_API_VALIDATOR";

    public BaseResponse map(final BindingResult bindingResult) {

        var fieldErrorDetails = extractErrors(bindingResult.getFieldErrors(),
                fieldError -> Objects.nonNull(fieldError.getDefaultMessage()),
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        var globalErrorDetails = extractErrors(bindingResult.getGlobalErrors(),
                objectError -> Objects.nonNull(objectError.getDefaultMessage()),
                Collectors.toMap(ObjectError::getObjectName, ObjectError::getDefaultMessage));

        fieldErrorDetails.putAll(globalErrorDetails);

        var error = AppError.builder()
                            .httpStatusCode(BAD_REQUEST.value())
                            .message("Invalid payload")
                            .origin(API_VALIDATOR)
                            .details(fieldErrorDetails)
                            .build();

        return BaseResponse.builder()
                .error(error)
                .build();
    }

    private <T extends ObjectError> Map<String, String> extractErrors(final List<T> errors,
                                              final Predicate<T> filterFunction,
                                              final Collector<T, ?, Map<String, String>> collector) {

        return Optional.of(errors)
                .orElse(List.of())
                .stream()
                .filter(filterFunction)
                .collect(collector);
    }

}
