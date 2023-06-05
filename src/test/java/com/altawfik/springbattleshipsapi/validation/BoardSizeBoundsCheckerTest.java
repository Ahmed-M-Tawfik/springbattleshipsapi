package com.altawfik.springbattleshipsapi.validation;

import com.altawfik.springbattleshipsapi.error.InvalidBoardPositionException;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class BoardSizeBoundsCheckerTest {
    private BoardSizeBoundsChecker boardSizeBoundsChecker;

    @BeforeEach
    void setup() {
        boardSizeBoundsChecker = spy(BoardSizeBoundsChecker.class);
    }

    @Test
    void validatePositionWithinBoardBounds_FrontInvalidXCoordinate_ThrowsException() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(-1, 5);

        // Act & Assert
        assertThrows(InvalidBoardPositionException.class, () ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord));

        verify(boardSizeBoundsChecker, never()).validatePositionWithinBoardBounds(any(BoardSize.class), any(BoardCoordinate.class), any(ShipOrientation.class), anyInt());
    }

    @Test
    void validatePositionWithinBoardBounds_FrontInvalidYCoordinate_ThrowsException() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(5, -1);

        // Act & Assert
        assertThrows(InvalidBoardPositionException.class, () ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord));

        verify(boardSizeBoundsChecker, never()).validatePositionWithinBoardBounds(any(BoardSize.class), any(BoardCoordinate.class), any(ShipOrientation.class), anyInt());
    }

    @Test
    void validatePositionWithinBoardBounds_FrontInvalidCoordinates_ThrowsException() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(11, 11);

        // Act & Assert
        assertThrows(InvalidBoardPositionException.class, () ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord));

        verify(boardSizeBoundsChecker, never()).validatePositionWithinBoardBounds(any(BoardSize.class), any(BoardCoordinate.class), any(ShipOrientation.class), anyInt());
    }

    @Test
    void validatePositionWithinBoardBounds_FrontValidCoordinates_NoExceptionsThrown() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(5, 5);

        // Act & Assert
        assertDoesNotThrow(() ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord));

        verify(boardSizeBoundsChecker, never()).validatePositionWithinBoardBounds(any(BoardSize.class), any(BoardCoordinate.class), any(ShipOrientation.class), anyInt());
    }

    @Test
    void validatePositionWithinBoardBounds_InvalidBackCoordinatesHorizontalLeft_ThrowsException() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_LEFT;
        int shipLength = 6; // Invalid length that exceeds the board boundaries

        // Act & Assert
        assertThrows(InvalidBoardPositionException.class, () ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord, shipOrientation, shipLength));

        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, coord);
        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, new BoardCoordinate(coord.getX(), coord.getY() + shipLength));
    }

    @Test
    void validatePositionWithinBoardBounds_InvalidBackCoordinatesHorizontalRight_ThrowsException() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_RIGHT;
        int shipLength = 7; // Invalid length that exceeds the board boundaries

        // Act & Assert
        assertThrows(InvalidBoardPositionException.class, () ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord, shipOrientation, shipLength));

        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, coord);
        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, new BoardCoordinate(coord.getX(), coord.getY() - shipLength));
    }

    @Test
    void validatePositionWithinBoardBounds_InvalidBackCoordinatesVerticalUp_ThrowsException() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.VERTICAL_UP;
        int shipLength = 7; // Invalid length that exceeds the board boundaries

        // Act & Assert
        assertThrows(InvalidBoardPositionException.class, () ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord, shipOrientation, shipLength));

        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, coord);
        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, new BoardCoordinate(coord.getX() + shipLength, coord.getY()));
    }

    @Test
    void validatePositionWithinBoardBounds_InvalidBackCoordinatesVerticalDown_ThrowsException() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.VERTICAL_DOWN;
        int shipLength = 7; // Invalid length that exceeds the board boundaries

        // Act & Assert
        assertThrows(InvalidBoardPositionException.class, () ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord, shipOrientation, shipLength));

        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, coord);
        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, new BoardCoordinate(coord.getX() - shipLength, coord.getY()));
    }

    @Test
    void validatePositionWithinBoardBounds_ValidShipPlacementCoordinates_NoExceptionsThrown() {
        // Arrange
        BoardSize boardSize = new BoardSize(10, 10);
        BoardCoordinate coord = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_LEFT;
        int shipLength = 3;

        // Act & Assert
        assertDoesNotThrow(() ->
                boardSizeBoundsChecker.validatePositionWithinBoardBounds(boardSize, coord, shipOrientation, shipLength));

        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, coord);
        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(boardSize, new BoardCoordinate(coord.getX(), coord.getY() + shipLength));
    }
}
