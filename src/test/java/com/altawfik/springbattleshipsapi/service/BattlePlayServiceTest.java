package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayRoundRequest;
import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattlePlayServiceTest {

    private BattlePlayService battlePlayService;
    @Mock
    private BattleValidatorRetriever battleValidatorRetriever;

    @BeforeEach
    void setUp() {
        battlePlayService = new BattlePlayService(battleValidatorRetriever);
    }

    @Test
    void asasdasd() {
        var battleId = UUID.randomUUID();
        var playRoundRequest = new PlayRoundRequest(PlayerNumber.PLAYER_ONE, new BoardCoordinate(1, 1));

        var battle = new Battle(new BoardSize(10, 10));

        when(battleValidatorRetriever.validateAndRetrieveBattle(battleId, BattleState.InPlay)).thenReturn(battle);

        battlePlayService.playRound(battleId, playRoundRequest);
    }
}