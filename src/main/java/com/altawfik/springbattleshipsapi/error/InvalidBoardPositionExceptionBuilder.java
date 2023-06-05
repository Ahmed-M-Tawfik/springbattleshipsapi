package com.altawfik.springbattleshipsapi.error;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import org.springframework.http.HttpStatus;

public class InvalidBoardPositionExceptionBuilder extends ExceptionBuilder<InvalidBoardPositionExceptionBuilder> {

    public InvalidBoardPositionExceptionBuilder(final BoardSize boardSize, final BoardCoordinate boardCoordinate) {
        super(HttpStatus.BAD_REQUEST, "Invalid board position for operation. Board size is " + boardSize.x() +
                ", " + boardSize.y() + " and requested invalid coordinates are " + boardCoordinate.getX() + ", " +
                boardCoordinate.getY());
    }

    public InvalidBoardPositionException build() {
        return new InvalidBoardPositionException(message, cause, httpStatus, details, origin);
    }
}
