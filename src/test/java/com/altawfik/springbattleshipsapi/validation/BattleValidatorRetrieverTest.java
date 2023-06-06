package com.altawfik.springbattleshipsapi.validation;

import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.BoardSize;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleValidatorRetrieverTest {

    private BattleValidatorRetriever battleValidatorRetriever;
    @Mock
    private BattleRepository battleRepository;

    @BeforeEach
    public void setup() {
        battleValidatorRetriever = new BattleValidatorRetriever(battleRepository);
    }

    @Test
    public void validBattle_retrievedAndReturned() {
        UUID battleId = UUID.randomUUID();

        Battle expectedBattle = new Battle(new BoardSize(10, 10));
        when(battleRepository.getBattle(battleId)).thenReturn(expectedBattle);

        Battle actualBattle = battleValidatorRetriever.validateAndRetrieveBattle(battleId, BattleState.Initialisation);
        assertThat(expectedBattle).isSameAs(actualBattle);
    }

    @Test
    public void invalidBattleDoesntExist_throwException() {
        UUID battleId = UUID.randomUUID();

        when(battleRepository.getBattle(battleId)).thenReturn(null);

        var exception = assertThrows(ContentException.class,
                () -> battleValidatorRetriever.validateAndRetrieveBattle(battleId, BattleState.Initialisation));
        assertThat(exception.getMessage()).isEqualTo(String.format("Battle with UUID %s not found", battleId));
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void invalidBattleState_throwException() {
        UUID battleId = UUID.randomUUID();

        Battle battle = new Battle(new BoardSize(10, 10));
        when(battleRepository.getBattle(battleId)).thenReturn(battle);

        var exception = assertThrows(ContentException.class,
                () -> battleValidatorRetriever.validateAndRetrieveBattle(battleId, BattleState.InPlay));
        assertThat(exception.getMessage()).isEqualTo("Invalid state for operation. Current state is " + battle.getState());
        assertThat(exception.getHttpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}