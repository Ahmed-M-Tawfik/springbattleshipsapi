package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import static org.assertj.core.api.Assertions.assertThat;

class BindingResultExceptionMapperTest {

    private BindingResultExceptionMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new BindingResultExceptionMapper();
    }

    @Test
    public void givenBindingResultWithGlobalErrors_whenMapCalled_thenErrorHasGlobalErrors() {
        // Given
        String objectName = "testObject";
        String defaultMessage = "Object has an error";
        ObjectError objectError = new ObjectError(objectName, defaultMessage);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(objectError);

        // When
        BaseResponse response = mapper.map(bindingResult);

        // Then
        assertThat(response.getError().getDetails().get(objectName)).isEqualTo(defaultMessage);
    }

    @Test
    public void givenBindingResultWithFieldErrors_whenMapCalled_thenErrorHasFieldErrors() {
        // Given
        String fieldName = "testField";
        String defaultMessage = "Field has an error";
        FieldError fieldError = new FieldError("testObject", fieldName, defaultMessage);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(fieldError);

        // When
        BaseResponse response = mapper.map(bindingResult);

        // Then
        assertThat(response.getError().getDetails().get(fieldName)).isEqualTo(defaultMessage);
    }

    @Test
    public void givenBindingResultWithMixedErrors_whenMapCalled_thenErrorHasAllErrors() {
        // Given
        String objectName = "testObject";
        String defaultMessage = "Object has an error";
        ObjectError objectError = new ObjectError(objectName, defaultMessage);
        String fieldName = "testField";
        String defaultMessage2 = "Field has an error";
        FieldError fieldError = new FieldError("testObject", fieldName, defaultMessage2);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(objectError);
        bindingResult.addError(fieldError);

        // When
        BaseResponse response = mapper.map(bindingResult);

        // Then
        assertThat(response.getError().getDetails().get(objectName)).isEqualTo(defaultMessage);
        assertThat(response.getError().getDetails().get(fieldName)).isEqualTo(defaultMessage2);
    }

    @Test
    public void givenEmptyBindingResult_whenMapCalled_thenErrorHasDefaultValues() {
        // Given
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");

        // When
        BaseResponse response = mapper.map(bindingResult);

        // Then
        assertThat(response.getError().getDetails()).isEmpty();
        assertThat(response.getError().getMessage()).isEqualTo("Invalid payload");
        assertThat(response.getError().getOrigin()).isEqualTo(BindingResultExceptionMapper.API_VALIDATOR);
        assertThat(response.getError().getHttpStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void givenBindingResultWithNullFieldErrors_whenMapCalled_thenErrorHasGlobalErrors() {
        // Given
        String objectName = "testObject";
        String defaultMessage = "Object has an error";
        ObjectError objectError = new ObjectError(objectName, defaultMessage);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(objectError);

        // When
        BaseResponse response = mapper.map(bindingResult);

        // Then
        assertThat(response.getError().getDetails().get(objectName)).isEqualTo(defaultMessage);
    }

    @Test
    public void givenBindingResultWithNullGlobalErrors_whenMapCalled_thenErrorHasFieldErrors() {
        // Given
        String fieldName = "testField";
        String defaultMessage = "Field has an error";
        FieldError fieldError = new FieldError("testObject", fieldName, defaultMessage);
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(fieldError);

        // When
        BaseResponse response = mapper.map(bindingResult);

        // Then
        assertThat(response.getError().getDetails().get(fieldName)).isEqualTo(defaultMessage);
    }
}