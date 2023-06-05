package com.altawfik.springbattleshipsapi.error;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import org.springframework.http.HttpStatus;

public class BoardPositionAlreadyTakenExceptionBuilder extends ExceptionBuilder<BoardPositionAlreadyTakenExceptionBuilder> {

    public BoardPositionAlreadyTakenExceptionBuilder(final BoardCoordinate boardCoordinate) {
        super(HttpStatus.BAD_REQUEST, "Cannot place new entity at given position " + boardCoordinate.getX() + ", " +
                boardCoordinate.getY() + ". Entity exists at that location.");
    }

    public BoardPositionAlreadyTakenException build() {
        return new BoardPositionAlreadyTakenException(message, cause, httpStatus, details, origin);
    }
}
