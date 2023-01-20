package com.altawfik.springbattleshipsapi.service.shipconfig;

import com.altawfik.springbattleshipsapi.model.Ship;
import com.altawfik.springbattleshipsapi.service.ShipConfigurationProvider;
import org.springframework.stereotype.Service;

import static com.altawfik.springbattleshipsapi.service.shipconfig.ShipConfigurationProviderUtils.populateShip;

@Service
public class SparseShipConfigurationProvider implements ShipConfigurationProvider {
    @Override
    public Ship[] getShips() {
        return new Ship[] {
                populateShip("Small Ship", 2),
                populateShip("Submarine", 2),
                populateShip("Medium Ship", 3),
        };
    }
}
