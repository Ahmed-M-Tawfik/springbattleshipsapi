package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.model.Board;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.boardentity.BoardEntity;
import com.altawfik.springbattleshipsapi.model.boardentity.Miss;
import com.altawfik.springbattleshipsapi.model.boardentity.ShipSection;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BoardHitPlacer {

    private static final Miss MISS = new Miss();

    public void placeHit(Board oppositePlayerBoard, BoardCoordinate targetCoord) {
        BoardEntity boardEntity = oppositePlayerBoard.grid()[targetCoord.getX()][targetCoord.getY()];

        // mark a miss
        if(!(boardEntity instanceof ShipSection targetedShipSection)) {
            oppositePlayerBoard.grid()[targetCoord.getX()][targetCoord.getY()] = MISS;
            return;
        }

        // mark section as hit
        targetedShipSection.setHit(true);

        // if all ship sections hit, mark it as sunk
        int count = (int) Arrays.stream(targetedShipSection.getParentShip().getShipSections())
                .filter(parentShipSection -> !parentShipSection.isHit()).count();
        if(count == 0) {
            targetedShipSection.getParentShip().setSunk(true);
        }
    }
}
