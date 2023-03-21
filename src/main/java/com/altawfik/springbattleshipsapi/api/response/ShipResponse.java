package com.altawfik.springbattleshipsapi.api.response;

public record ShipResponse(String shipName, ShipSectionResponse[] shipSection) {
}
