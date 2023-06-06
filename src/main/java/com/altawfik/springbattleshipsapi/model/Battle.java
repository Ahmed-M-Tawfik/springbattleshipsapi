package com.altawfik.springbattleshipsapi.model;

import lombok.Getter;
import lombok.Setter;

import static com.altawfik.springbattleshipsapi.config.GameConstants.PLAYERS_PER_BATTLE;

@Getter
@Setter
public class Battle {

    private final Player[] players;
    private final Board[] boards;
    private final BoardSize boardSize;

    private int roundCounter = 0;

    private Player winningPlayer = null;

    private BattleState state;

    public Battle(BoardSize boardSize) {
        players = new Player[PLAYERS_PER_BATTLE];
        boards = new Board[PLAYERS_PER_BATTLE];
        this.boardSize = boardSize;

        for(int i = 0; i < PLAYERS_PER_BATTLE; i++) {
            boards[i] = new Board(boardSize);
        }

        state = BattleState.Initialisation;
    }

    public void setPlayer(final int playerNumber, final String playerName, Ship[] ships) {
        players[playerNumber] = new Player(playerName, ships);
    }

    public void incrementRoundCounter() {
        roundCounter++;
    }
}
