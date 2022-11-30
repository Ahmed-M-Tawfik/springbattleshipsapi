package com.altawfik.springbattleshipsapi;

import com.altawfik.springbattleshipsapi.model.Battle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class BattleRepositoryTest {
    private BattleRepository battleRepository;

    @BeforeEach
    public void setUp() {
        battleRepository = new BattleRepository();
    }

    @Test
    public void shouldPopulateDataStoreOnCall() {
        UUID id = battleRepository.newBattle();
        assertThat(id).isNotNull();

        Battle battle = battleRepository.getBattle(id);
        assertThat(battle).isNotNull();
    }
}