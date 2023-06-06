package com.altawfik.springbattleshipsapi.model;

import com.altawfik.springbattleshipsapi.model.boardentity.ShipSection;
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
    private boolean sunk;

    public Ship(String shipName, ShipSection[] shipSections) {
        this(shipName, shipSections, false, false);
    }
}