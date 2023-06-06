package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayRoundRequest;
import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.Board;
import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.validation.BoardSizeBoundsChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.altawfik.springbattleshipsapi.config.GameConstants.PLAYERS_PER_BATTLE;

@RequiredArgsConstructor
@Service
public class BattlePlayService {

    private static final BattleState BATTLE_PLAY_STATE = BattleState.InPlay;

    private final BattleValidatorRetriever battleValidatorRetriever;
    private final BoardSizeBoundsChecker boardSizeBoundsChecker;
    private final BoardHitPlacer boardHitPlacer;
    private final WinConditionEvaluator winConditionEvaluator;

    public void playRound(UUID battleId, PlayRoundRequest playRoundRequest) {
        Battle battle = battleValidatorRetriever.validateAndRetrieveBattle(battleId, BATTLE_PLAY_STATE);

        var playerNumForRound = getPlayerNumForRound(battle.getRoundCounter());
        if(!playRoundRequest.playerNumber().equals(playerNumForRound)) {
            throw new ContentExceptionBuilder(String.format("Incorrect round for player. It is %s's turn", playerNumForRound)).build();
        }

        BoardCoordinate targetCoord = playRoundRequest.targetLocation();
        boardSizeBoundsChecker.validatePositionWithinBoardBounds(battle.getBoardSize(), targetCoord);

        boardHitPlacer.placeHit(getOppositePlayerBoard(battle, playerNumForRound), targetCoord);

        battle.incrementRoundCounter();

        winConditionEvaluator.evaluateRound(battle);
    }

    PlayerNumber getPlayerNumForRound(int round) {
        return PlayerNumber.values()[round % PLAYERS_PER_BATTLE];
    }

    Board getOppositePlayerBoard(Battle battle, PlayerNumber playerNumForRound) {
        return battle.getBoards()[(playerNumForRound.ordinal() + 1) % PLAYERS_PER_BATTLE];
    }
}
