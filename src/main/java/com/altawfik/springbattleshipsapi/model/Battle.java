package com.altawfik.springbattleshipsapi.model;

public class Battle {

    private final Player[] players;

    private BattleState state;

    public Battle() {
        players = new Player[2];
        state = BattleState.Initialisation;
    }

    public void setPlayer(final int playerNumber, final String playerName, Ship[] ships) {
        players[playerNumber] = new Player(playerName, ships);
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setState(final BattleState state) {
        this.state = state;
    }

    public BattleState getState() {
        return state;
    }
}
