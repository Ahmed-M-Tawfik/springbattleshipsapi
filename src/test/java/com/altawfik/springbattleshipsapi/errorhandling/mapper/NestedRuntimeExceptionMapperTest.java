package com.altawfik.springbattleshipsapi.errorhandling.mapper;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.NestedRuntimeException;

public class NestedRuntimeExceptionMapperTest {

    @Test
    public void testMapJsonProcessingException() {
        NestedRuntimeExceptionMapper mapper = new NestedRuntimeExceptionMapper();
        JsonProcessingException exception = new JsonProcessingException("Test message") {};
        NestedRuntimeException nestedException = new NestedRuntimeException("Test message", exception) {
            // Create a subclass of NestedRuntimeException for testing purposes
        };
        BaseResponse result = mapper.map(nestedException);
        Assertions.assertEquals(result.getError().getHttpStatusCode(), 400);
        Assertions.assertEquals(result.getError().getMessage(), "Invalid payload");
        Assertions.assertEquals(result.getError().getOrigin(), "PAYLOAD_API_VALIDATOR");
        Assertions.assertEquals(result.getError().getDetails().get("rootCause"), "Test message");
    }

    @Test
    public void testMapNestedRuntimeException() {
        NestedRuntimeExceptionMapper mapper = new NestedRuntimeExceptionMapper();
        NestedRuntimeException exception = new NestedRuntimeException("Test message") {
            // Create a subclass of NestedRuntimeException for testing purposes
        };
        BaseResponse result = mapper.map(exception);
        Assertions.assertEquals(result.getError().getHttpStatusCode(), 400);
        Assertions.assertEquals(result.getError().getMessage(), "Invalid payload");
        Assertions.assertEquals(result.getError().getOrigin(), "PAYLOAD_API_VALIDATOR");
        Assertions.assertEquals(result.getError().getDetails().get("rootCause"), "Test message");
    }
}
