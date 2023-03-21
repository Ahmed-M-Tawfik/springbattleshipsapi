package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.mapper.BattleToBattleResponseMapper;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundException;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
        UUID id = UUID.randomUUID();

        Battle battleMock = mock(Battle.class);
        BattleResponse battleResponseMock = mock(BattleResponse.class);

        when(battleRepository.getBattle(id)).thenReturn(battleMock);
        when(battleResponseMapper.map(battleMock)).thenReturn(battleResponseMock);

        BattleResponse actualBattleResponse = battleRetrievalService.getBattle(id);

        assertThat(actualBattleResponse).isEqualTo(battleResponseMock);
    }

    @Test
    public void shouldThrowExceptionWhenBattleDoesNotExist() {
        UUID id = UUID.randomUUID();

        var expectedException = new BattleNotFoundExceptionBuilder(id).build();
        when(battleRepository.getBattle(id)).thenThrow(expectedException);

        BattleNotFoundException actualException = assertThrows(BattleNotFoundException.class,
                () -> battleRetrievalService.getBattle(id));

        assertThat(actualException.getMessage()).isEqualTo(String.format(BattleNotFoundExceptionBuilder.NOT_FOUND_MESSAGE, id));
        verify(battleResponseMapper, times(0)).map(any());
    }
}