package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayRoundRequest;
import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import com.altawfik.springbattleshipsapi.validation.BoardSizeBoundsChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattlePlayServiceTest {

    private BattlePlayService battlePlayService;
    @Mock
    private BattleValidatorRetriever battleValidatorRetriever;
    @Mock
    private BoardSizeBoundsChecker boardSizeBoundsChecker;
    @Mock
    private BoardHitPlacer boardHitPlacer;
    @Mock
    private WinConditionEvaluator winConditionEvaluator;

    @BeforeEach
    void setUp() {
        battlePlayService = new BattlePlayService(battleValidatorRetriever, boardSizeBoundsChecker, boardHitPlacer, winConditionEvaluator);
    }

    @Test
    void calculateRound() {
        assertThat(battlePlayService.getPlayerNumForRound(0)).isEqualTo(PlayerNumber.PLAYER_ONE);
        assertThat(battlePlayService.getPlayerNumForRound(1)).isEqualTo(PlayerNumber.PLAYER_TWO);
        assertThat(battlePlayService.getPlayerNumForRound(2)).isEqualTo(PlayerNumber.PLAYER_ONE);
    }

    @Test
    void playRound_ValidRequest_BattleUpdated() {
        var battleId = UUID.randomUUID();
        var playRoundRequest = new PlayRoundRequest(PlayerNumber.PLAYER_ONE, new BoardCoordinate(1, 1));

        var battle = new Battle(new BoardSize(10, 10));

        when(battleValidatorRetriever.validateAndRetrieveBattle(battleId, BattleState.InPlay)).thenReturn(battle);

        battlePlayService.playRound(battleId, playRoundRequest);

        verify(boardSizeBoundsChecker).validatePositionWithinBoardBounds(battle.getBoardSize(), playRoundRequest.targetLocation());
        verify(boardHitPlacer).placeHit(battle.getBoards()[1], playRoundRequest.targetLocation());
        verify(winConditionEvaluator).evaluateRound(battle);
    }

    @Test
    void playRound_InvalidPlayerTurn_ThrowException() {
        var battleId = UUID.randomUUID();
        var playRoundRequest = new PlayRoundRequest(PlayerNumber.PLAYER_ONE, new BoardCoordinate(1, 1));
        var battle = new Battle(new BoardSize(10, 10));
        battle.incrementRoundCounter();

        when(battleValidatorRetriever.validateAndRetrieveBattle(battleId, BattleState.InPlay)).thenReturn(battle);

        var exception = assertThrows(ContentException.class, () -> battlePlayService.playRound(battleId, playRoundRequest));

        assertThat(exception.getMessage()).isEqualTo(String.format("Incorrect round for player. It is %s's turn", PlayerNumber.PLAYER_TWO));
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);

        verifyNoInteractions(boardSizeBoundsChecker);
        verifyNoInteractions(boardHitPlacer);
        verifyNoInteractions(winConditionEvaluator);
    }
}