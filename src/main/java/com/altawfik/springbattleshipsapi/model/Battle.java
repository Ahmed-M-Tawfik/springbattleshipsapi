package com.altawfik.springbattleshipsapi.model;

import com.altawfik.springbattleshipsapi.api.request.PlayerNumber;

public class Battle {

    private final Player[] players;

    public Battle() {
        players = new Player[2];
    }

    public void setPlayer(final int playerNumber, final String playerName) {
        players[playerNumber] = new Player(playerName);
    }

    public Player[] getPlayers() {
        return players;
    }
}
