package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import com.altawfik.springbattleshipsapi.model.Board;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.boardentity.BoardEntity;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;
import com.altawfik.springbattleshipsapi.model.boardentity.ShipSection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardPlacerTest {

    private BoardPlacer boardPlacer;

    @BeforeEach
    void setUp() {
        boardPlacer = new BoardPlacer();
    }

    @Test
    void placeShipOnBoard_HorizontalRight_PlacesShipOnBoard() {
        // Arrange
        BoardEntity[][] grid = new BoardEntity[10][10];
        Board board = new Board(grid);
        Ship ship = createShip("Ship", 3);
        BoardCoordinate boardCoordinate = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_RIGHT;

        // Act
        boardPlacer.placeShipOnBoard(board, ship, boardCoordinate, shipOrientation);

        // Assert
        for (int i = 0; i < ship.getShipSections().length; i++) {
            assertThat(grid[5][5 + i]).isInstanceOf(ShipSection.class);
            assertTrue(grid[5][5 + i] instanceof ShipSection);
            assertEquals(ship.getShipSections()[i], grid[5][5 + i]);
        }
    }

    @Test
    void placeShipOnBoard_HorizontalLeft_PlacesShipOnBoard() {
        // Arrange
        BoardEntity[][] grid = new BoardEntity[10][10];
        Board board = new Board(grid);
        Ship ship = createShip("Ship", 3);
        BoardCoordinate boardCoordinate = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_LEFT;

        // Act
        boardPlacer.placeShipOnBoard(board, ship, boardCoordinate, shipOrientation);

        // Assert
        for (int i = 0; i < ship.getShipSections().length; i++) {
            assertThat(grid[5][5 - i]).isInstanceOf(ShipSection.class);
            assertEquals(ship.getShipSections()[i], grid[5][5 - i]);
        }
    }

    @Test
    void placeShipOnBoard_VerticalUp_PlacesShipOnBoard() {
        // Arrange
        BoardEntity[][] grid = new BoardEntity[10][10];
        Board board = new Board(grid);
        Ship ship = createShip("Ship", 3);
        BoardCoordinate boardCoordinate = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.VERTICAL_UP;

        // Act
        boardPlacer.placeShipOnBoard(board, ship, boardCoordinate, shipOrientation);

        // Assert
        for (int i = 0; i < ship.getShipSections().length; i++) {
            assertThat(grid[5 - i][5]).isInstanceOf(ShipSection.class);
            assertEquals(ship.getShipSections()[i], grid[5 - i][5]);
        }
    }

    @Test
    void placeShipOnBoard_VerticalDown_PlacesShipOnBoard() {
        // Arrange
        BoardEntity[][] grid = new BoardEntity[10][10];
        Board board = new Board(grid);
        Ship ship = createShip("Ship", 3);
        BoardCoordinate boardCoordinate = new BoardCoordinate(5, 5);
        ShipOrientation shipOrientation = ShipOrientation.VERTICAL_DOWN;

        // Act
        boardPlacer.placeShipOnBoard(board, ship, boardCoordinate, shipOrientation);

        // Assert
        for (int i = 0; i < ship.getShipSections().length; i++) {
            assertThat(grid[5 + i][5]).isInstanceOf(ShipSection.class);
            assertEquals(ship.getShipSections()[i], grid[5 + i][5]);
        }
    }

    @Test
    void placeShipOnBoard_PositionAlreadyTaken_ThrowsException() {
        // Arrange
        BoardEntity[][] grid = new BoardEntity[10][10];
        Board board = new Board(grid);
        Ship ship = createShip("Ship", 3);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_RIGHT;
        BoardCoordinate boardCoordinate = new BoardCoordinate(5, 5);
        BoardCoordinate occupiedPosition = new BoardCoordinate(5, 6);
        grid[occupiedPosition.getX()][occupiedPosition.getY()] = new ShipSection(ship); // Already occupied position

        // Act & Assert
        var actualException = assertThrows(ContentException.class, () ->
                boardPlacer.placeShipOnBoard(board, ship, boardCoordinate, shipOrientation));
        assertThat(actualException.getMessage()).isEqualTo("Cannot place new entity at given position " + occupiedPosition.getX() + ", " +
                occupiedPosition.getY() + ". Entity exists at that location.");
        assertThat(actualException.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private Ship createShip(String shipName, int length) {
        ShipSection[] shipSections = new ShipSection[length];
        Ship ship = new Ship(shipName, shipSections);

        for (int i = 0; i < length; i++) {
            shipSections[i] = new ShipSection(ship);
        }

        return ship;
    }
}
