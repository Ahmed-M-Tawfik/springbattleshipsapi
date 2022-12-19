package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundException;
import com.altawfik.springbattleshipsapi.error.InvalidBattleStateException;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameException;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleInitialisationServiceTest {

    private BattleInitialisationService battleInitialisationService;

    @Mock
    private BattleRepository battleRepository;

    @BeforeEach
    public void setUp() {
        battleInitialisationService = new BattleInitialisationService(battleRepository);
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
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        Battle battle = new Battle();
        when(battleRepository.getBattle(uuid)).thenReturn(battle);

        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());
    }

    @Test
    public void shouldAllowInitOfBothPlayers() {
        UUID uuid = UUID.randomUUID();

        Battle battle = new Battle();
        when(battleRepository.getBattle(uuid)).thenReturn(battle);

        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, "playerName2");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        assertThat(battle.getPlayers()[1].playerName()).isEqualTo(playerSetupRequest.playerName());
    }

    @Test
    public void shouldAllowInitSamePlayerMultipleTimes() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        Battle battle = new Battle();
        when(battleRepository.getBattle(uuid)).thenReturn(battle);

        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName2");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName3");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo(playerSetupRequest.playerName());
    }

    @Test
    public void whenInitPlayerWithPrecedingOrTrailingWhitespaceThenNameMustBeTrimmed() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "   playerName   ");

        Battle battle = new Battle();
        when(battleRepository.getBattle(uuid)).thenReturn(battle);

        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        assertThat(battle.getPlayers()[0].playerName()).isEqualTo("playerName");
    }

    @Test
    public void whenInitPlayerWithBlankNameThenThrowInvalidPlayerNameException() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, " ");

        var e = assertThrows(InvalidPlayerNameException.class,
                             () -> battleInitialisationService.initPlayer(uuid, playerSetupRequest));

        assertThat(e.getMessage()).isEqualTo("Invalid player name provided:  ");
        assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verify(battleRepository, times(0)).getBattle(uuid);
    }

    @Test
    public void givenNonInitStateWhenInitThenThrowInvalidBattleStateException() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        Battle battle = new Battle();
        battle.setState(BattleState.Concluded);
        when(battleRepository.getBattle(uuid)).thenReturn(battle);

        var e = assertThrows(InvalidBattleStateException.class,
                             () -> battleInitialisationService.initPlayer(uuid, playerSetupRequest));

        assertThat(e.getMessage()).isEqualTo("Invalid state for operation. Current state is " + battle.getState());
        assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenNoBattleExistsForIdWhenInitThenThrowBattleNotFoundException() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        when(battleRepository.getBattle(uuid)).thenReturn(null);

        var e = assertThrows(BattleNotFoundException.class,
                             () -> battleInitialisationService.initPlayer(uuid, playerSetupRequest));

        assertThat(e.getMessage()).isEqualTo("Battle with UUID " + uuid + " not found");
        assertThat(e.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}