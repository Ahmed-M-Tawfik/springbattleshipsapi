package com.altawfik.springbattleshipsapi.error;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import org.springframework.http.HttpStatus;

public class InvalidBattleStateExceptionBuilder extends ExceptionBuilder<InvalidBattleStateExceptionBuilder> {

    public InvalidBattleStateExceptionBuilder(final BattleState currentState) {
        super(HttpStatus.BAD_REQUEST, "Invalid state for operation. Current state is " + currentState);
    }

    public InvalidBattleStateException build() {
        return new InvalidBattleStateException(message, cause, httpStatus, details, origin);
    }
}
