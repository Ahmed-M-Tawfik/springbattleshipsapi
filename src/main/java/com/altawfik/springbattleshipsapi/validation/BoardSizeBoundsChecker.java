package com.altawfik.springbattleshipsapi.validation;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;
import org.springframework.stereotype.Component;

@Component
public class BoardSizeBoundsChecker {
    public void validatePositionWithinBoardBounds(BoardSize boardSize, BoardCoordinate boardCoordinate) {
        if(boardCoordinate.getX() > boardSize.x()-1 || boardCoordinate.getX() < 0 ||
                boardCoordinate.getY() > boardSize.y()-1 || boardCoordinate.getY() < 0) {
            throw new ContentExceptionBuilder("Invalid board position for operation. " +
                    "Board size is " + boardSize.x() + ", " + boardSize.y() +
                    " and requested invalid coordinates are " + boardCoordinate.getX() + ", " + boardCoordinate.getY()).build();
        }
    }

    public void validatePositionWithinBoardBounds(BoardSize boardSize,
                                                         BoardCoordinate boardCoordinate,
                                                         ShipOrientation shipOrientation,
                                                         int length) {
        validatePositionWithinBoardBounds(boardSize, boardCoordinate);

        BoardCoordinate otherEndCoordinate = null;
        switch(shipOrientation) {
            // calculate from front of ship to back; boardCoordinate is front-most ship section location
            case HORIZONTAL_LEFT -> otherEndCoordinate = new BoardCoordinate(boardCoordinate.getX(), boardCoordinate.getY() + length);
            case HORIZONTAL_RIGHT -> otherEndCoordinate = new BoardCoordinate(boardCoordinate.getX(), boardCoordinate.getY() - length);
            case VERTICAL_UP -> otherEndCoordinate = new BoardCoordinate(boardCoordinate.getX() + length, boardCoordinate.getY());
            case VERTICAL_DOWN -> otherEndCoordinate = new BoardCoordinate(boardCoordinate.getX() - length, boardCoordinate.getY());
        }

        validatePositionWithinBoardBounds(boardSize, otherEndCoordinate);
    }
}
