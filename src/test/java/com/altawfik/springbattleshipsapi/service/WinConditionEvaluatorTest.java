package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import com.altawfik.springbattleshipsapi.service.shipconfig.TestShipConfigurationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WinConditionEvaluatorTest {

    private WinConditionEvaluator winConditionEvaluator;

    private final TestShipConfigurationProvider shipConfig = new TestShipConfigurationProvider();

    @BeforeEach
    void setUp() {
        winConditionEvaluator = new WinConditionEvaluator();
    }

    @Test
    void evaluation_WinCondition() {
        Battle battle = new Battle(new BoardSize(10, 10));
        battle.setPlayer(PlayerNumber.PLAYER_ONE.ordinal(), "P1", shipConfig.getPlacedAndSunkShips());
        battle.setPlayer(PlayerNumber.PLAYER_TWO.ordinal(), "P2", shipConfig.getPlacedShips());

        winConditionEvaluator.evaluateRound(battle);

        assertThat(battle.getState()).isEqualTo(BattleState.Concluded);
        assertThat(battle.getWinningPlayer()).isSameAs(battle.getPlayers()[1]);
    }

    @Test
    void evaluation_GameplayContinues() {
        Battle battle = new Battle(new BoardSize(10, 10));
        battle.setState(BattleState.InPlay);
        battle.setPlayer(PlayerNumber.PLAYER_ONE.ordinal(), "P1", shipConfig.getPlacedShips());
        battle.setPlayer(PlayerNumber.PLAYER_TWO.ordinal(), "P2", shipConfig.getPlacedShips());

        winConditionEvaluator.evaluateRound(battle);

        assertThat(battle.getState()).isEqualTo(BattleState.InPlay);
        assertThat(battle.getWinningPlayer()).isNull();
    }
}