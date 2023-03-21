package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.mapper.BattleToBattleResponseMapper;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundException;
import com.altawfik.springbattleshipsapi.error.BattleNotFoundExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BattleRetrievalService {

    private final BattleRepository battleRepository;
    private final BattleToBattleResponseMapper battleResponseMapper;

    public BattleResponse getBattle(UUID battleId) {
        Battle retrievedBattle = battleRepository.getBattle(battleId);
        if(retrievedBattle == null) {
            throw new BattleNotFoundExceptionBuilder(battleId).build();
        }
        return battleResponseMapper.map(retrievedBattle);
    }
}
