package com.altawfik.springbattleshipsapi.api.response;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.errorhandling.model.AppError;
import com.altawfik.springbattleshipsapi.model.Ship;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ResponseTest {
    @Test
    public void testBaseResponse() {
        var baseResponse = new BaseResponse(new AppError());
        assertNotNull(baseResponse.getError());
        var baseResponse2 = new BaseResponse(baseResponse.getError());
        assertNotNull(baseResponse2.getError());
        var baseResponse3 = new BaseResponse();
        baseResponse3.toBuilder().error(baseResponse.getError()).build();
        assertNotNull(baseResponse2.getError());
    }

    @Test
    public void testBattleResponse() {
        var battleResponse = new BattleResponse(
                new PlayerResponse[]{},
                new BoardResponse[]{},
                new BoardSizeResponse(1, 1),
                BattleStateResponse.Initialisation
        );
        var battleResponse2 = new BattleResponse(
                battleResponse.getPlayers(),
                battleResponse.getBoards(),
                battleResponse.getBoardSize(),
                battleResponse.getState()
        );
        assertNotNull(battleResponse);
        assertEquals(battleResponse, battleResponse2);
        var battleResponse3 = new BattleResponse(
                battleResponse.getPlayers(),
                battleResponse.getBoards(),
                battleResponse.getBoardSize(),
                BattleStateResponse.Concluded
        );
        assertNotEquals(battleResponse, battleResponse3);
    }

    @Test
    public void testPlayerResponse() {
        String name = "name";
        var playerResponse = new PlayerResponse(name, new Ship[]{});
        assertEquals(name, playerResponse.playerName());
        assertEquals(0, playerResponse.ships().length);
    }

    @Test
    public void testShipResponse() {
        String name = "name";
        var shipResponse = new ShipResponse(name, new ShipSectionResponse[]{});
        assertEquals(name, shipResponse.shipName());
        assertEquals(0, shipResponse.shipSection().length);
    }

    @Test
    public void testShipSectionResponse() {
        var shipSectionResponse = new ShipSectionResponse(true);
        var shipSectionResponse2 = new ShipSectionResponse(false);
        shipSectionResponse2.setHit();
        assertNotNull(shipSectionResponse);
        assertEquals(shipSectionResponse, shipSectionResponse2);
        var shipSectionResponse3 = new ShipSectionResponse(false);
        assertNotEquals(shipSectionResponse, shipSectionResponse3);
    }

    @Test
    public void testBoardResponse() {
        var boardResponse = new BoardResponse(new BoardEntityResponse[][]{});
        assertEquals(0, boardResponse.grid().length);
    }

    @Test
    public void testBoardSizeResponse() {
        var boardSizeResponse = new BoardSizeResponse(1, 1);
        var boardSizeResponse2 = new BoardSizeResponse(1, 1);
        assertNotNull(boardSizeResponse);
        assertEquals(boardSizeResponse, boardSizeResponse2);
        var boardSizeResponse3 = new BoardSizeResponse(1, 2);
        assertNotEquals(boardSizeResponse, boardSizeResponse3);
    }
}