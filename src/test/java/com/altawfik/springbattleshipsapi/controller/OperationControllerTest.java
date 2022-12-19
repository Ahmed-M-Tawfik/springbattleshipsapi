package com.altawfik.springbattleshipsapi.controller;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.service.BattleInitialisationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperationControllerTest {

    private OperationController controller;

    @Mock
    private BattleInitialisationService battleInitialisationService;

    @BeforeEach
    public void setUp() {
        controller = new OperationController(battleInitialisationService);
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
}