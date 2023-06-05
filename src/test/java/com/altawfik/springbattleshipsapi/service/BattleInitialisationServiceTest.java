package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.api.request.ShipPlacementRequest;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundException;
import com.altawfik.springbattleshipsapi.error.InvalidBattleStateException;
import com.altawfik.springbattleshipsapi.error.InvalidBoardPositionException;
import com.altawfik.springbattleshipsapi.error.InvalidBoardPositionExceptionBuilder;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameException;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;
import com.altawfik.springbattleshipsapi.model.ShipSection;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import com.altawfik.springbattleshipsapi.service.shipconfig.SparseShipConfigurationProvider;
import com.altawfik.springbattleshipsapi.service.shipconfig.StandardShipConfigurationProvider;
import com.altawfik.springbattleshipsapi.validation.BoardSizeBoundsChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleInitialisationServiceTest {

    private BattleInitialisationService battleInitialisationService;

    @Mock
    private BattleRepository battleRepository;
    @Mock
    private BoardPlacer boardPlacer;
    @Mock
    private BoardSizeBoundsChecker boardSizeBoundsChecker;

    private final ShipConfigurationProvider shipConfigurationProviderSpy = spy(new SparseShipConfigurationProvider());

    @BeforeEach
    public void setUp() {
        battleInitialisationService = new BattleInitialisationService(battleRepository, shipConfigurationProviderSpy, boardPlacer,
                boardSizeBoundsChecker);
    }

    @Test
    public void shouldGenerateNewUuidWhenCreatingNewBattle() {
        UUID expectedId = UUID.randomUUID();

        when(battleRepository.newBattle()).thenReturn(expectedId);

        UUID actualID = battleInitialisationService.newBattle();

        assertThat(actualID).isEqualTo(expectedId);
    }

    @Test
    public void whenInitPlayerThenRetrieveBattleAndPopulateCorrectPlayer() {
        UUID battleId = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        Battle battle = new Battle(new BoardSize(9, 9));
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        battleInitialisationService.initPlayer(battleId, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        verify(shipConfigurationProviderSpy).getShips();
    }

    @Test
    public void shouldAllowInitOfBothPlayers() {
        UUID battleId = UUID.randomUUID();

        Battle battle = new Battle(new BoardSize(9, 9));
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");
        battleInitialisationService.initPlayer(battleId, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        verify(shipConfigurationProviderSpy).getShips();

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, "playerName2");
        battleInitialisationService.initPlayer(battleId, playerSetupRequest);

        assertThat(battle.getPlayers()[1].playerName()).isEqualTo(playerSetupRequest.playerName());

        verify(shipConfigurationProviderSpy, times(2)).getShips();
    }

    @Test
    public void shouldAllowInitSamePlayerMultipleTimes() {
        UUID battleId = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        Battle battle = new Battle(new BoardSize(9, 9));
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        battleInitialisationService.initPlayer(battleId, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        verify(shipConfigurationProviderSpy).getShips();

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName2");
        battleInitialisationService.initPlayer(battleId, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        verify(shipConfigurationProviderSpy, times(2)).getShips();

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName3");
        battleInitialisationService.initPlayer(battleId, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        verify(shipConfigurationProviderSpy, times(3)).getShips();
    }

    @Test
    public void whenInitPlayerWithPrecedingOrTrailingWhitespaceThenNameMustBeTrimmed() {
        UUID battleId = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "   playerName   ");

        Battle battle = new Battle(new BoardSize(9, 9));
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        battleInitialisationService.initPlayer(battleId, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo("playerName");

        verify(shipConfigurationProviderSpy).getShips();
    }

    @Test
    public void whenInitPlayerWithBlankNameThenThrowInvalidPlayerNameException() {
        UUID battleId = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, " ");

        var e = assertThrows(InvalidPlayerNameException.class,
                             () -> battleInitialisationService.initPlayer(battleId, playerSetupRequest));

        assertThat(e.getMessage()).isEqualTo("Invalid player name provided:  ");
        assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(battleRepository, times(0)).getBattle(battleId);
        verify(shipConfigurationProviderSpy, times(0)).getShips();
    }

    @Test
    public void givenNonInitStateWhenInitPlayerThenThrowInvalidBattleStateException() {
        UUID battleId = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        Battle battle = new Battle(new BoardSize(9, 9));
        battle.setState(BattleState.Concluded);
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        var e = assertThrows(InvalidBattleStateException.class,
                             () -> battleInitialisationService.initPlayer(battleId, playerSetupRequest));

        assertThat(e.getMessage()).isEqualTo("Invalid state for operation. Current state is " + battle.getState());
        assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenNoBattleExistsForIdWhenInitPlayerThenThrowBattleNotFoundException() {
        UUID battleId = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        when(battleRepository.getBattle(battleId)).thenReturn(null);

        var e = assertThrows(BattleNotFoundException.class,
                             () -> battleInitialisationService.initPlayer(battleId, playerSetupRequest));

        assertThat(e.getMessage()).isEqualTo("Battle with UUID " + battleId + " not found");
        assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void placeShipOnBoard_ValidPlacement_NoExceptionsThrown() {
        UUID battleId = UUID.randomUUID();
        PlayerNumber num = PlayerNumber.PLAYER_ONE;
        var validBoardCoord = new BoardCoordinate(0, 0);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_LEFT;
        int shipListIndex = 0;
        var shipPlacementRequest = new ShipPlacementRequest(shipListIndex, shipOrientation, validBoardCoord);

        Battle battle = new Battle(new BoardSize(9, 9));
        Ship[] ships = new StandardShipConfigurationProvider().getShips();
        battle.setPlayer(num.ordinal(), "someName", ships);
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        assertDoesNotThrow(() -> battleInitialisationService.placeShipOnBoard(battleId, num, shipPlacementRequest));
        assertThat(ships[shipListIndex].isPlaced()).isTrue();

        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(battle.getBoardSize(), validBoardCoord, shipOrientation,
                ships[shipListIndex].getShipSections().length);
        verify(boardPlacer).placeShipOnBoard(battle.getBoards()[num.ordinal()], ships[shipListIndex], validBoardCoord, shipOrientation);
    }

    @Test
    public void placeShipOnBoard_ShipAlreadyPlaced_ExceptionThrown() {
        UUID battleId = UUID.randomUUID();
        PlayerNumber num = PlayerNumber.PLAYER_ONE;
        var invalidBoardCoord = new BoardCoordinate(0, 0);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_LEFT;
        int shipListIndex = 0;
        var shipPlacementRequest = new ShipPlacementRequest(shipListIndex, shipOrientation, invalidBoardCoord);

        Battle battle = new Battle(new BoardSize(9, 9));
        Ship[] ships = new StandardShipConfigurationProvider().getShips();
        ships[shipListIndex].setPlaced(true);
        battle.setPlayer(num.ordinal(), "someName", ships);
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        ContentException expectedException = new ContentExceptionBuilder("Ship already placed: " + ships[shipListIndex].getShipName()).build();
        ContentException actualException = assertThrows(ContentException.class,
                () -> battleInitialisationService.placeShipOnBoard(battleId, num, shipPlacementRequest));
        assertThat(actualException.getHttpStatus()).isEqualTo(expectedException.getHttpStatus());
        assertThat(actualException.getMessage()).isEqualTo(expectedException.getMessage());

        verifyNoInteractions(boardSizeBoundsChecker);
        verifyNoInteractions(boardPlacer);
    }

    @Test
    public void placeShipOnBoard_InvalidPlacement_InvalidBoardPositionExceptionThrown() {
        UUID battleId = UUID.randomUUID();
        PlayerNumber num = PlayerNumber.PLAYER_ONE;
        var invalidBoardCoord = new BoardCoordinate(0, 0);
        ShipOrientation shipOrientation = ShipOrientation.HORIZONTAL_LEFT;
        int shipListIndex = 0;
        var shipPlacementRequest = new ShipPlacementRequest(shipListIndex, shipOrientation, invalidBoardCoord);

        Battle battle = new Battle(new BoardSize(9, 9));
        Ship[] ships = new StandardShipConfigurationProvider().getShips();
        battle.setPlayer(num.ordinal(), "someName", ships);
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        InvalidBoardPositionException expectedException = new InvalidBoardPositionExceptionBuilder(battle.getBoardSize(), invalidBoardCoord).build();
        doThrow(expectedException)
                .when(boardSizeBoundsChecker).validatePositionWithinBoardBounds(battle.getBoardSize(), invalidBoardCoord, shipOrientation, ships[shipListIndex].getShipSections().length);

        InvalidBoardPositionException actualException = assertThrows(InvalidBoardPositionException.class,
                () -> battleInitialisationService.placeShipOnBoard(battleId, num, shipPlacementRequest));
        assertThat(actualException).isSameAs(expectedException);
        assertThat(ships[shipListIndex].isPlaced()).isFalse();

        verifyNoInteractions(boardPlacer);
    }

    @Test
    public void getUnplacedShips() {
        UUID battleId = UUID.randomUUID();
        PlayerNumber playerNumber = PlayerNumber.PLAYER_ONE;
        Ship[] expectedShips = new Ship[]{new Ship("Ship1", new ShipSection[]{}, false),
                new Ship("Ship2", new ShipSection[]{}, false)};

        Battle battle = new Battle(new BoardSize(10, 10));
        battle.setPlayer(playerNumber.ordinal(), "playerName", expectedShips);

        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        Ship[] unplacedShips = battleInitialisationService.getUnplacedShips(battleId, playerNumber);

        assertThat(unplacedShips).isEqualTo(expectedShips);
    }
}