package com.altawfik.springbattleshipsapi.service.shipconfig;

import com.altawfik.springbattleshipsapi.model.Ship;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ShipConfigurationProviderUtilsTest {

    @Test
    public void should() {
        String shipName = "shipName";
        int numberOfShipSections = 2;

        Ship shipResult = ShipConfigurationProviderUtils.populateShip(shipName, numberOfShipSections);

        assertThat(shipResult.getShipName()).isEqualTo(shipName);
        assertThat(shipResult.getShipSections().length).isEqualTo(numberOfShipSections);
    }
}