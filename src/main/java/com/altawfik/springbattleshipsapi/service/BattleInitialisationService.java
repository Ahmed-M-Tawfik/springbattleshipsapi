package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BattleInitialisationService {

    private final BattleRepository battleRepository;

    public BattleInitialisationService(final BattleRepository battleRepository) {
        this.battleRepository = battleRepository;
    }

    public UUID newBattle() {
        return battleRepository.newBattle();
    }

    public void initPlayer(final UUID battleId, final PlayerSetupRequest playerSetupRequest) {
        if(playerSetupRequest.playerName().isBlank()) {
            throw new InvalidPlayerNameExceptionBuilder(playerSetupRequest.playerName()).build();
        }

        Battle currentBattle = battleRepository.getBattle(battleId);
        currentBattle.setPlayer(playerSetupRequest.playerNumber().ordinal(), playerSetupRequest.playerName().trim());
    }
}
