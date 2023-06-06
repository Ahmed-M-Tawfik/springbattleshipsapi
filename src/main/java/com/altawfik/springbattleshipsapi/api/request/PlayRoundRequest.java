package com.altawfik.springbattleshipsapi.api.request;

import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;

public record PlayRoundRequest(PlayerNumber playerNumber, BoardCoordinate targetLocation) {
}
