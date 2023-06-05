package com.altawfik.springbattleshipsapi.service.shipconfig;

import com.altawfik.springbattleshipsapi.model.Ship;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StandardShipConfigurationProviderTest {

    @Test
    void shouldCreateFiveSmallMediumAndLargeShipsIncludingCarrier() {
        Ship[] shipsResult = new StandardShipConfigurationProvider().getShips();

        assertThat(shipsResult).hasSize(5);
        assertThat(shipsResult[4].getShipName()).isEqualTo("Aircraft Carrier");
    }
}