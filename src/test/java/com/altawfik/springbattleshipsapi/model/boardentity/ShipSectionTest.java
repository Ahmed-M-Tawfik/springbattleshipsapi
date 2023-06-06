package com.altawfik.springbattleshipsapi.model.boardentity;

import com.altawfik.springbattleshipsapi.model.Ship;
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
