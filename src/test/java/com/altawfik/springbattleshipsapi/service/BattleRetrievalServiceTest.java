package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.mapper.BattleToBattleResponseMapper;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentException;
import com.altawfik.springbattleshipsapi.model.Battle;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BattleRetrievalServiceTest {
    private BattleRetrievalService battleRetrievalService;

    @BeforeEach
    public void setup() {
        battleRetrievalService = new BattleRetrievalService(battleRepository, battleResponseMapper);
    }

    @Mock
    private BattleRepository battleRepository;
    @Mock
    private BattleToBattleResponseMapper battleResponseMapper;

    @Test
    public void shouldRetrieveBattleWhenExists() {
        UUID battleId = UUID.randomUUID();

        Battle battleMock = mock(Battle.class);
        BattleResponse battleResponseMock = mock(BattleResponse.class);

        when(battleRepository.getBattle(battleId)).thenReturn(battleMock);
        when(battleResponseMapper.map(battleMock)).thenReturn(battleResponseMock);

        BattleResponse actualBattleResponse = battleRetrievalService.getBattle(battleId);

        assertThat(actualBattleResponse).isEqualTo(battleResponseMock);
    }

    @Test
    public void shouldThrowExceptionWhenBattleDoesNotExist() {
        UUID battleId = UUID.randomUUID();

        ContentException actualException = assertThrows(ContentException.class,
                () -> battleRetrievalService.getBattle(battleId));

        assertThat(actualException.getMessage()).isEqualTo(String.format("Battle with UUID %s not found", battleId));
        assertThat(actualException.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(battleRepository).getBattle(battleId);
        verify(battleResponseMapper, times(0)).map(any(Battle.class));
    }
}