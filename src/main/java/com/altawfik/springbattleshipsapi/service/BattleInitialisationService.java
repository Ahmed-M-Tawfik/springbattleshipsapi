package com.altawfik.springbattleshipsapi.service;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;
import com.altawfik.springbattleshipsapi.api.request.PlayerSetupRequest;
import com.altawfik.springbattleshipsapi.api.request.ShipPlacementRequest;
import com.altawfik.springbattleshipsapi.errorhandling.exception.ContentExceptionBuilder;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.Player;
import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.repository.BattleRepository;
import com.altawfik.springbattleshipsapi.validation.BoardSizeBoundsChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BattleInitialisationService {

    private static final BattleState BATTLE_INIT_STATE = BattleState.Initialisation;

    private final BattleRepository battleRepository;
    private final BattleValidatorRetriever battleValidatorRetriever;
    private final ShipConfigurationProvider shipConfigurationProvider;
    private final BoardPlacer boardPlacer;
    private final BoardSizeBoundsChecker boardSizeBoundsChecker;

    public UUID newBattle() {
        return battleRepository.newBattle();
    }

    public void initPlayer(final UUID battleId, final PlayerSetupRequest playerSetupRequest) {
        if (playerSetupRequest.playerName().isBlank()) {
            throw new ContentExceptionBuilder("Invalid player name provided: " + playerSetupRequest.playerName()).build();
        }

        Battle battle = battleValidatorRetriever.validateAndRetrieveBattle(battleId, BATTLE_INIT_STATE);

        battle.setPlayer(playerSetupRequest.playerNumber().ordinal(),
                                playerSetupRequest.playerName().trim(),
                                shipConfigurationProvider.getShips());
    }

    public Ship[] getUnplacedShips(final UUID battleId, final PlayerNumber playerNumber) {
        Battle battle = battleValidatorRetriever.validateAndRetrieveBattle(battleId, BATTLE_INIT_STATE);
        Player player = battle.getPlayers()[playerNumber.ordinal()];
        Ship[] ships = player.ships();

        return Arrays.stream(ships).filter(ship -> !ship.isPlaced()).toArray(Ship[]::new);
    }

    public void placeShipOnBoard(UUID battleId, PlayerNumber playerNumber, ShipPlacementRequest shipPlacementRequest) {
        Battle battle = battleValidatorRetriever.validateAndRetrieveBattle(battleId, BATTLE_INIT_STATE);
        Ship ship = battle.getPlayers()[playerNumber.ordinal()].ships()[shipPlacementRequest.shipListIndex()];
        if(ship.isPlaced())
            throw new ContentExceptionBuilder("Ship already placed: " + ship.getShipName()).build();

        boardSizeBoundsChecker.validatePositionWithinBoardBounds(battle.getBoardSize(),
                shipPlacementRequest.boardCoordinate(),
                shipPlacementRequest.shipOrientation(),
                ship.getShipSections().length);

        boardPlacer.placeShipOnBoard(battle.getBoards()[playerNumber.ordinal()],
                ship,
                shipPlacementRequest.boardCoordinate(),
                shipPlacementRequest.shipOrientation());

        ship.setPlaced(true);
    }

    public void startBattle(UUID battleId) {
        Battle battle = battleValidatorRetriever.validateAndRetrieveBattle(battleId, BATTLE_INIT_STATE);
        Player[] players = battle.getPlayers();

        if(players[0] == null || players[1] == null) {
            throw new ContentExceptionBuilder("Both players must be initialised").build();
        }
        for(Player player : players) {
            Ship[] ships = player.ships();
            var numOfUnplacedShips = Arrays.stream(ships).filter(ship -> !ship.isPlaced()).count();
            if(numOfUnplacedShips > 0)
                throw new ContentExceptionBuilder("Player " + player.playerName() + " still has ships to be placed").build();
        }

        battle.setState(BattleState.InPlay);
    }
}