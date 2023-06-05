package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.mapper.BattleToBattleResponseMapper;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            throw new ContentExceptionBuilder(HttpStatus.NOT_FOUND, String.format("Battle with UUID %s not found", battleId)).build();
        }
        return battleResponseMapper.map(retrievedBattle);
    }
}
