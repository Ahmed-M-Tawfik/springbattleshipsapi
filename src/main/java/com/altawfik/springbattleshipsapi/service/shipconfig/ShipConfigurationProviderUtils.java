package com.altawfik.springbattleshipsapi.service.shipconfig;

import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.model.ShipSection;

class ShipConfigurationProviderUtils {
    static Ship populateShip(String shipName, int numberOfShipSections) {
        ShipSection[] shipSections = new ShipSection[numberOfShipSections];
        Ship ship = new Ship(shipName, shipSections);

        for(int i = 0; i < numberOfShipSections; i++) {
            shipSections[i] = new ShipSection(ship);
        }

        return ship;
    }
}
