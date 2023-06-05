package com.altawfik.springbattleshipsapi.service.shipconfig;

import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.service.ShipConfigurationProvider;
import org.springframework.stereotype.Service;

import java.util.Arrays;

import static com.altawfik.springbattleshipsapi.service.shipconfig.ShipConfigurationProviderUtils.populateShip;

@Service
public class TestShipConfigurationProvider implements ShipConfigurationProvider {

    private boolean shipsPlaced = false;

    public TestShipConfigurationProvider setShipsPlaced() {
        this.shipsPlaced = true;
        return this;
    }

    @Override
    public Ship[] getShips() {
        var ships = new Ship[] {
                populateShip("Small Ship", 2),
                populateShip("Submarine", 3),
                populateShip("Medium Ship", 3),
                populateShip("Large Ship", 4),
                populateShip("Aircraft Carrier", 5)
        };

        if(shipsPlaced)
            Arrays.stream(ships).forEach(ship -> ship.setPlaced(shipsPlaced));

        return ships;
    }
}
