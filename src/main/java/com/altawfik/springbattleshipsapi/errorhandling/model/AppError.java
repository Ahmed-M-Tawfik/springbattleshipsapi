package com.altawfik.springbattleshipsapi.errorhandling.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Jacksonized
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
public class AppError {

    private int httpStatusCode;

    private String message;

    private String origin;

    private Map<String, String> details;
}
