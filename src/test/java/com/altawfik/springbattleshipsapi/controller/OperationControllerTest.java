package com.altawfik.springbattleshipsapi.controller;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.api.request.PlayRoundRequest;
import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.api.request.ShipPlacementRequest;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.service.BattleInitialisationService;
import com.altawfik.springbattleshipsapi.service.BattlePlayService;
import com.altawfik.springbattleshipsapi.service.BattleRetrievalService;
import com.altawfik.springbattleshipsapi.service.shipconfig.TestShipConfigurationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationControllerTest {

    private OperationController controller;

    @Mock
    private BattleInitialisationService battleInitialisationService;
    @Mock
    private BattleRetrievalService battleRetrievalService;
    @Mock
    private BattlePlayService battlePlayService;

    @BeforeEach
    public void setUp() {
        controller = new OperationController(battleInitialisationService, battleRetrievalService, battlePlayService);
    }

    @Test
    public void shouldAllocateNewBattleUponRequest() {
        UUID expectedId = UUID.randomUUID();
        when(battleInitialisationService.newBattle()).thenReturn(expectedId);

        ResponseEntity<UUID> response = controller.allocateNewBattle();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedId);
    }

    @Test
    public void shouldReturnOkWhenInitialisingPlayer() {
        UUID id = UUID.randomUUID();
        String playerName = "playerName";
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, playerName);

        ResponseEntity<BaseResponse> response = controller.setPlayerName(id, playerSetupRequest);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(battleInitialisationService).initPlayer(id, playerSetupRequest);
    }

    @Test
    public void shouldReturnOkWhenRetrievingExistingBattle() {
        UUID id = UUID.randomUUID();

        var battleResponseMock = mock(BattleResponse.class);
        when(battleRetrievalService.getBattle(id)).thenReturn(battleResponseMock);

        ResponseEntity<BattleResponse> response = controller.retrieveBattle(id);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(battleResponseMock);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void placeShipOnBoard_ValidPlacement() {
        // Arrange
        UUID battleId = UUID.randomUUID();
        PlayerNumber playerNumber = PlayerNumber.PLAYER_ONE;
        ShipPlacementRequest shipPlacementRequest = null; // can't mock, and inconsequential

        // Act
        ResponseEntity<BaseResponse> response = controller.placeShipOnBoard(battleId, playerNumber, shipPlacementRequest);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(battleInitialisationService).placeShipOnBoard(battleId, playerNumber, shipPlacementRequest);
    }

    @Test
    public void shouldReturnUnplacedShips() {
        UUID battleId = UUID.randomUUID();
        PlayerNumber playerNumber = PlayerNumber.PLAYER_ONE;
        Ship[] expectedShips = new TestShipConfigurationProvider().getShips();

        when(battleInitialisationService.getUnplacedShips(battleId, playerNumber)).thenReturn(expectedShips);

        ResponseEntity<Ship[]> response = controller.getUnplacedShips(battleId, playerNumber);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expectedShips);
    }

    @Test
    public void startBattle() {
        UUID battleId = UUID.randomUUID();
        BattleResponse battleResponse = new BattleResponse(null, null, null, null); // unneeded values
        when(battleRetrievalService.getBattle(battleId)).thenReturn(battleResponse);

        ResponseEntity<BattleResponse> response = controller.startBattle(battleId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(battleResponse);

        verify(battleInitialisationService).startBattle(battleId);
    }

    @Test
    public void playRound() {
        UUID battleId = UUID.randomUUID();
        var playRoundRequest = new PlayRoundRequest(PlayerNumber.PLAYER_ONE, new BoardCoordinate(1, 1));
        BattleResponse battleResponse = new BattleResponse(null, null, null, null); // unneeded values
        when(battleRetrievalService.getBattle(battleId)).thenReturn(battleResponse);

        ResponseEntity<BattleResponse> response = controller.playRound(battleId, playRoundRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isSameAs(battleResponse);

        verify(battlePlayService).playRound(battleId, playRoundRequest);
    }
}