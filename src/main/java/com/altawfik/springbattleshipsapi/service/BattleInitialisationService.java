package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundExceptionBuilder;
import com.altawfik.springbattleshipsapi.error.InvalidBattleStateExceptionBuilder;
import com.altawfik.springbattleshipsapi.error.InvalidPlayerNameExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BattleInitialisationService {

    private static final BattleState BATTLE_INIT_STATE = BattleState.Initialisation;

    private final BattleRepository battleRepository;
    private final ShipConfigurationProvider shipConfigurationProvider;

    public BattleInitialisationService(final BattleRepository battleRepository, final ShipConfigurationProvider shipConfigurationProvider) {
        this.battleRepository = battleRepository;
        this.shipConfigurationProvider = shipConfigurationProvider;
    }

    public UUID newBattle() {
        return battleRepository.newBattle();
    }

    public void initPlayer(final UUID battleId, final PlayerSetupRequest playerSetupRequest) {
        if (playerSetupRequest.playerName().isBlank()) {
            throw new InvalidPlayerNameExceptionBuilder(playerSetupRequest.playerName()).build();
        }

        Battle currentBattle = validateAndRetrieveBattle(battleId);

        currentBattle.setPlayer(playerSetupRequest.playerNumber().ordinal(),
                                playerSetupRequest.playerName().trim(),
                                shipConfigurationProvider.getShips());
    }

    public Ship[] getUnplacedShips(final UUID battleId, final PlayerNumber playerNumber) {
        throw new NotImplementedException();
    }

    private Battle validateAndRetrieveBattle(final UUID battleId) {
        Battle currentBattle = battleRepository.getBattle(battleId);
        if (currentBattle == null) {
            throw new BattleNotFoundExceptionBuilder(battleId).build();
        }

        if (!currentBattle.getState().equals(BATTLE_INIT_STATE)) {
            throw new InvalidBattleStateExceptionBuilder(currentBattle.getState()).build();
        }
        return currentBattle;
    }
}