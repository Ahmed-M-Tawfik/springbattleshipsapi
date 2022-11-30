package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameException;
import com.altawfik.springbattleshipsapi.model.Battle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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

        Battle mockedBattle = mock(Battle.class);
        when(battleRepository.getBattle(uuid)).thenReturn(mockedBattle);

        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        verify(mockedBattle).setPlayer(0, playerSetupRequest.playerName());
    }

    @Test
    public void shouldAllowInitOfBothPlayers() {
        UUID uuid = UUID.randomUUID();

        Battle mockedBattle = mock(Battle.class);
        when(battleRepository.getBattle(uuid)).thenReturn(mockedBattle);

        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        verify(mockedBattle).setPlayer(0, playerSetupRequest.playerName());

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_TWO, "playerName2");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        verify(mockedBattle).setPlayer(1, playerSetupRequest.playerName());
    }

    @Test
    public void shouldAllowInitSamePlayerMultipleTimes() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName");

        Battle mockedBattle = mock(Battle.class);
        when(battleRepository.getBattle(uuid)).thenReturn(mockedBattle);

        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        verify(mockedBattle).setPlayer(0, playerSetupRequest.playerName());

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName2");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        verify(mockedBattle).setPlayer(0, playerSetupRequest.playerName());

        playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "playerName3");
        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        verify(mockedBattle).setPlayer(0, playerSetupRequest.playerName());
    }

    @Test
    public void whenInitPlayerWithPrecedingOrTrailingWhitespaceThenNameMustBeTrimmed() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, "   playerName   ");

        Battle mockedBattle = mock(Battle.class);
        when(battleRepository.getBattle(uuid)).thenReturn(mockedBattle);

        battleInitialisationService.initPlayer(uuid, playerSetupRequest);

        verify(mockedBattle).setPlayer(0, "playerName");
    }

    @Test
    public void whenInitPlayerWithBlankNameThenThrowInvalidPlayerNameException() {
        UUID uuid = UUID.randomUUID();
        PlayerSetupRequest playerSetupRequest = new PlayerSetupRequest(PlayerNumber.PLAYER_ONE, " ");

        var e = assertThrows(InvalidPlayerNameException.class,
                             () -> battleInitialisationService.initPlayer(uuid, playerSetupRequest));

        assertThat(e.getMessage()).isEqualTo("Invalid player name provided:  ");

        verify(battleRepository, times(0)).getBattle(uuid);
    }
}