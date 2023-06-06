package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.model.Board;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import com.altawfik.springbattleshipsapi.model.boardentity.Miss;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.model.boardentity.ShipSection;
import com.altawfik.springbattleshipsapi.service.shipconfig.TestShipConfigurationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BoardHitPlacerTest {

    private BoardHitPlacer boardHitPlacer;

    private final TestShipConfigurationProvider shipConfig = new TestShipConfigurationProvider();

    @BeforeEach
    void setUp() {
        boardHitPlacer = new BoardHitPlacer();
    }

    @Test
    void targetShip_singleHit() {
        Board oppositeBoard = new Board(new BoardSize(5, 5));
        BoardCoordinate targetCoord = new BoardCoordinate(0, 2);

        Ship targetShip = shipConfig.getSingleShip(3);
        ShipSection targetShipSection = targetShip.getShipSections()[2];
        oppositeBoard.grid()[targetCoord.getX()][targetCoord.getY()] = targetShipSection;

        assertThat(targetShipSection.isHit()).isFalse();
        assertThat(targetShip.isSunk()).isFalse();

        boardHitPlacer.placeHit(oppositeBoard, targetCoord);

        assertThat(targetShipSection.isHit()).isTrue();
        assertThat(targetShip.isSunk()).isFalse();
    }

    @Test
    void targetShip_shipSunk() {
        Board oppositeBoard = new Board(new BoardSize(5, 5));
        BoardCoordinate targetCoord = new BoardCoordinate(0, 2);

        Ship targetShip = shipConfig.getSingleShip(3);
        targetShip.getShipSections()[0].setHit(true);
        targetShip.getShipSections()[1].setHit(true);
        ShipSection targetShipSection = targetShip.getShipSections()[2];
        oppositeBoard.grid()[targetCoord.getX()][targetCoord.getY()] = targetShipSection;

        assertThat(targetShipSection.isHit()).isFalse();
        assertThat(targetShip.isSunk()).isFalse();

        boardHitPlacer.placeHit(oppositeBoard, targetCoord);

        assertThat(targetShipSection.isHit()).isTrue();
        assertThat(targetShip.isSunk()).isTrue();
    }

    @Test
    void targetEmpty_miss() {
        Board oppositeBoard = new Board(new BoardSize(5, 5));
        BoardCoordinate shipSectionCoord = new BoardCoordinate(0, 2);
        BoardCoordinate targetCoord = new BoardCoordinate(1, 2);

        Ship targetShip = shipConfig.getSingleShip(3);
        ShipSection targetShipSection = targetShip.getShipSections()[2];
        oppositeBoard.grid()[shipSectionCoord.getX()][shipSectionCoord.getY()] = targetShipSection;

        assertThat(targetShipSection.isHit()).isFalse();
        assertThat(targetShip.isSunk()).isFalse();
        assertThat(oppositeBoard.grid()[targetCoord.getX()][targetCoord.getY()]).isNull();

        boardHitPlacer.placeHit(oppositeBoard, targetCoord);

        assertThat(oppositeBoard.grid()[targetCoord.getX()][targetCoord.getY()]).isInstanceOf(Miss.class);
        assertThat(targetShip.isSunk()).isFalse();
        assertThat(targetShipSection.isHit()).isFalse();
    }
}