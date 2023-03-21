package com.altawfik.springbattleshipsapi.api.response;

import com.altawfik.springbattleshipsapi.model.Ship;

public record PlayerResponse(String playerName, Ship[] ships) {
}