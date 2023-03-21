package com.altawfik.springbattleshipsapi.error;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.BattleState;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class BattleNotFoundExceptionBuilder extends ExceptionBuilder<BattleNotFoundExceptionBuilder> {

    public static final String NOT_FOUND_MESSAGE = "Battle with UUID %s not found";

    public BattleNotFoundExceptionBuilder(final UUID uuid) {
        super(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, uuid));
    }

    public BattleNotFoundException build() {
        return new BattleNotFoundException(message, cause, httpStatus, details, origin);
    }
}
