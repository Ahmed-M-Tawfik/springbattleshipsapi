package com.altawfik.springbattleshipsapi.controller;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.api.request.ShipPlacementRequest;
import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import com.altawfik.springbattleshipsapi.service.BattleInitialisationService;
import com.altawfik.springbattleshipsapi.service.BattleRetrievalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final BattleRetrievalService battleRetrievalService;

    public OperationController(final BattleInitialisationService battleInitialisationService, final BattleRetrievalService battleRetrievalService) {
        this.battleInitialisationService = battleInitialisationService;
        this.battleRetrievalService = battleRetrievalService;
    }

    @GetMapping("/{battleId}")
    public ResponseEntity<BattleResponse> retrieveBattle(@PathVariable final UUID battleId) {
        return ResponseEntity.ok(battleRetrievalService.getBattle(battleId));
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

    @PutMapping("/initialise/{battleId}/player/{playerNumber}/board")
    public ResponseEntity<BaseResponse> placeShipOnBoard(@PathVariable final UUID battleId,
                                                         @PathVariable final PlayerNumber playerNumber,
                                                         @RequestBody @Valid final ShipPlacementRequest shipPlacementRequest) {
        battleInitialisationService.placeShipOnBoard(battleId, playerNumber, shipPlacementRequest);
        return ResponseEntity.ok(new BaseResponse());
    }

    @GetMapping("/initialise/{battleId}/player/{playerNumber}/unplaced-ships")
    public ResponseEntity<Ship[]> getUnplacedShips(@PathVariable UUID battleId, @PathVariable PlayerNumber playerNumber) {
        Ship[] unplacedShips = battleInitialisationService.getUnplacedShips(battleId, playerNumber);
        return ResponseEntity.ok(unplacedShips);
    }
}
