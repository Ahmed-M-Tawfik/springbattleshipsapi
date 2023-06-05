package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.error.BoardPositionAlreadyTakenExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Board;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;
import org.springframework.stereotype.Component;

@Component
public class BoardPlacer {

    public void placeShipOnBoard(Board board, Ship ship, BoardCoordinate boardCoordinate, ShipOrientation shipOrientation) {
        switch (shipOrientation) {
            case HORIZONTAL_RIGHT -> placeShipOnBoardHorizontalRight(board, ship, boardCoordinate);
            case HORIZONTAL_LEFT -> placeShipOnBoardHorizontalLeft(board, ship, boardCoordinate);
            case VERTICAL_UP -> placeShipOnBoardVerticalUp(board, ship, boardCoordinate);
            case VERTICAL_DOWN -> placeShipOnBoardVerticalDown(board, ship, boardCoordinate);
        }
    }

    private void placeShipOnBoardHorizontalRight(Board board, Ship ship, BoardCoordinate boardCoordinate) {
        int x = boardCoordinate.getX();
        int y = boardCoordinate.getY();
        for (int i = 0; i < ship.getShipSections().length; i++) {
            validateEmptyLocation(board, x, y + i);
            board.grid()[x][y + i] = ship.getShipSections()[i];
        }
    }

    private void placeShipOnBoardHorizontalLeft(Board board, Ship ship, BoardCoordinate boardCoordinate) {
        int x = boardCoordinate.getX();
        int y = boardCoordinate.getY();
        for (int i = 0; i < ship.getShipSections().length; i++) {
            validateEmptyLocation(board, x, y - i);
            board.grid()[x][y - i] = ship.getShipSections()[i];
        }
    }

    private void placeShipOnBoardVerticalUp(Board board, Ship ship, BoardCoordinate boardCoordinate) {
        int x = boardCoordinate.getX();
        int y = boardCoordinate.getY();
        for (int i = 0; i < ship.getShipSections().length; i++) {
            validateEmptyLocation(board, x - i, y);
            board.grid()[x - i][y] = ship.getShipSections()[i];
        }
    }

    private void placeShipOnBoardVerticalDown(Board board, Ship ship, BoardCoordinate boardCoordinate) {
        int x = boardCoordinate.getX();
        int y = boardCoordinate.getY();
        for (int i = 0; i < ship.getShipSections().length; i++) {
            validateEmptyLocation(board, x + i, y);
            board.grid()[x + i][y] = ship.getShipSections()[i];
        }
    }

    private void validateEmptyLocation(Board board, int x, int y) {
        if (board.grid()[x][y] != null) {
            throw new BoardPositionAlreadyTakenExceptionBuilder(new BoardCoordinate(x, y)).build();
        }
    }
}
