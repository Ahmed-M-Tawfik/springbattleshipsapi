package com.altawfik.springbattleshipsapi.service.shipconfig;

import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.service.ShipConfigurationProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.altawfik.springbattleshipsapi.service.shipconfig.ShipConfigurationProviderUtils.populateShip;

@Service
public class TestShipConfigurationProvider implements ShipConfigurationProvider {

    @Override
    public Ship[] getShips() {
        return new Ship[] {
                populateShip("Small Ship", 2),
                populateShip("Submarine", 3),
                populateShip("Medium Ship", 3),
                populateShip("Large Ship", 4),
                populateShip("Aircraft Carrier", 5)
        };
    }

    public Ship[] getPlacedShips() {
        var ships = getShips();
        Arrays.stream(ships).forEach(ship -> ship.setPlaced(true));
        return ships;
    }

    public Ship[] getPlacedAndSunkShips() {
        var ships = getPlacedShips();
        Arrays.stream(ships).forEach(ship -> ship.setSunk(true));
        return ships;
    }

    public Ship getSingleShip(int numberOfShipSections) {
        return populateShip("Some Ship", numberOfShipSections);
    }
}
