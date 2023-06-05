package com.altawfik.springbattleshipsapi.api.request;

import com.altawfik.springbattleshipsapi.model.BoardCoordinate;
import com.altawfik.springbattleshipsapi.model.ShipOrientation;

public record ShipPlacementRequest(int shipListIndex, ShipOrientation shipOrientation, BoardCoordinate boardCoordinate) {
}
