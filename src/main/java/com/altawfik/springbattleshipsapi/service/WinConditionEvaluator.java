package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.Player;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class WinConditionEvaluator {
    public void evaluateRound(Battle battle) {
        List<Player> remainingPlayers = stream(battle.getPlayers()).filter(
                player -> stream(player.ships()).anyMatch(ship -> !ship.isSunk())
        ).collect(Collectors.toList());

        // clear winner
        if(remainingPlayers.size() == 1) {
            battle.setState(BattleState.Concluded);
            battle.setWinningPlayer(remainingPlayers.get(0));
        }
    }
}
