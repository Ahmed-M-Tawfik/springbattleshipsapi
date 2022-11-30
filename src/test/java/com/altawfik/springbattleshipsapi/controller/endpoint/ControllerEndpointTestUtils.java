package com.altawfik.springbattleshipsapi.controller.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;

class ControllerEndpointTestUtils {
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
