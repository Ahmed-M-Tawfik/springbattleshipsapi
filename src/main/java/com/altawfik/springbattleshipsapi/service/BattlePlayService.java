package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayRoundRequest;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BattlePlayService {

    private static final BattleState BATTLE_PLAY_STATE = BattleState.InPlay;

    private final BattleValidatorRetriever battleValidatorRetriever;

    public void playRound(UUID battleId, PlayRoundRequest playRoundRequest) {
        Battle battle = battleValidatorRetriever.validateAndRetrieveBattle(battleId, BATTLE_PLAY_STATE);
    }
}
