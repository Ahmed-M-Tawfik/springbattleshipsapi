package com.altawfik.springbattleshipsapi.api;

import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
public class BaseResponse {
    @Schema(accessMode = READ_ONLY)
    private AppError error;
}
