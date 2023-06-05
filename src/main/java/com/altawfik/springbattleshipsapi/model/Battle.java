package com.altawfik.springbattleshipsapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Battle {

    private final Player[] players;
    private final Board[] boards;
    private final BoardSize boardSize;

    private final int playerCount = 2;

    private BattleState state;

    public Battle(BoardSize boardSize) {
        players = new Player[playerCount];
        boards = new Board[playerCount];
        this.boardSize = boardSize;

        for(int i = 0; i < playerCount; i++) {
            boards[i] = new Board(boardSize);
        }

        state = BattleState.Initialisation;
    }

    public void setPlayer(final int playerNumber, final String playerName, Ship[] ships) {
        players[playerNumber] = new Player(playerName, ships);
    }
}
