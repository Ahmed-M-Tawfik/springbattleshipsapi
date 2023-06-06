package com.altawfik.springbattleshipsapi.model;

import com.altawfik.springbattleshipsapi.model.boardentity.ShipSection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShipSectionTest {

    @Test
    public void testToString() {
        Ship ship = new Ship();
        ShipSection shipSection = new ShipSection(ship);
        shipSection.setHit(true);

        String expected = "ShipSection(isHit=true)";
        String actual = shipSection.toString();

        assertEquals(expected, actual);
    }
}
