package com.altawfik.springbattleshipsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ship {
    private final String shipName;
    private final ShipSection[] shipSections;
    private boolean placed;

    public Ship(String shipName, ShipSection[] shipSections) {
        this(shipName, shipSections, false);
    }
}