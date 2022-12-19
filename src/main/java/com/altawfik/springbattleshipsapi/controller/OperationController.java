package com.altawfik.springbattleshipsapi.controller;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.service.BattleInitialisationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/battle")
public class OperationController {

    private final BattleInitialisationService battleInitialisationService;

    public OperationController(final BattleInitialisationService battleInitialisationService) {
        this.battleInitialisationService = battleInitialisationService;
    }

    @PostMapping("/allocate")
    public ResponseEntity<UUID> allocateNewBattle() {
        return ResponseEntity.created(URI.create("/battle/initialise/")).body(battleInitialisationService.newBattle());
    }

    @PostMapping("/initialise/{battleId}/player")
    public ResponseEntity<BaseResponse> setPlayerName(@PathVariable final UUID battleId,
                                                      @RequestBody @Valid final PlayerSetupRequest playerSetupRequest) {
        battleInitialisationService.initPlayer(battleId, playerSetupRequest);
        return ResponseEntity.ok(new BaseResponse());
    }


}
