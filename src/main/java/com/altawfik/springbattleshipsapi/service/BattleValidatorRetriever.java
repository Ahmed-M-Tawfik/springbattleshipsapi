package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BattleValidatorRetriever {

    private final BattleRepository battleRepository;

    Battle validateAndRetrieveBattle(final UUID battleId, final BattleState requiredBattleState) {
        Battle retrievedBattle = battleRepository.getBattle(battleId);
        if (retrievedBattle == null) {
            throw new ContentExceptionBuilder(HttpStatus.NOT_FOUND, String.format("Battle with UUID %s not found", battleId)).build();
        }

        if (!retrievedBattle.getState().equals(requiredBattleState)) {
            throw new ContentExceptionBuilder("Invalid state for operation. Current state is " + retrievedBattle.getState()).build();
        }
        return retrievedBattle;
    }
}
