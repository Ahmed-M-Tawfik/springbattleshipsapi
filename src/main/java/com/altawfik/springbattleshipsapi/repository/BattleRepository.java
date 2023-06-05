package com.altawfik.springbattleshipsapi.repository;

import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class BattleRepository {
    private final Map<UUID, Battle> battleStore;

    public BattleRepository() {
        battleStore = new HashMap<>();
    }

    public UUID newBattle() {
        UUID newBattle = UUID.randomUUID();
        BoardSize boardSize = new BoardSize(5, 5);
        battleStore.put(newBattle, new Battle(boardSize));
        return newBattle;
    }

    public Battle getBattle(final UUID battleId) {
        return battleStore.get(battleId);
    }
}
