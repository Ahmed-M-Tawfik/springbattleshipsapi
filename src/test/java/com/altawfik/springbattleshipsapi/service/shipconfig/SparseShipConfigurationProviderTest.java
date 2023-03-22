package com.altawfik.springbattleshipsapi.service.shipconfig;

import com.altawfik.springbattleshipsapi.model.Ship;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SparseShipConfigurationProviderTest {

    @Test
    void shouldCreateThreeSmallMediumShips() {
        Ship[] shipsResult = new SparseShipConfigurationProvider().getShips();

        assertThat(shipsResult).hasSize(3);
    }
}