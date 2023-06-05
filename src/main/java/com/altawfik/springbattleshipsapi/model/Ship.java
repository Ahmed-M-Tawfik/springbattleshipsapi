package com.altawfik.springbattleshipsapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    private String shipName;
    private ShipSection[] shipSections;
    private boolean placed;

    public Ship(String shipName, ShipSection[] shipSections) {
        this(shipName, shipSections, false);
    }
}