package com.altawfik.springbattleshipsapi.api.mapper;

import com.altawfik.springbattleshipsapi.api.response.BoardEntityResponse;
import com.altawfik.springbattleshipsapi.api.response.ShipSectionResponse;
import com.altawfik.springbattleshipsapi.model.BoardEntity;
import com.altawfik.springbattleshipsapi.model.ShipSection;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BattleToBattleResponseMapperTest {

    private final BattleToBattleResponseMapper mapper = Mappers.getMapper(BattleToBattleResponseMapper.class);

    @Test
    public void testMapGridWithShipSection() {
        // Given
        BoardEntity[][] grid = new BoardEntity[2][2];
        grid[0][0] = new ShipSection();
        grid[0][1] = null;
        grid[1][0] = null;
        grid[1][1] = new ShipSection();

        // When
        BoardEntityResponse[][] gridResponse = mapper.map(grid);

        // Then
        assertNotNull(gridResponse);
        assertEquals(2, gridResponse.length);
        assertEquals(2, gridResponse[0].length);
        assertTrue(gridResponse[0][0] instanceof ShipSectionResponse);
        assertNull(gridResponse[0][1]);
        assertNull(gridResponse[1][0]);
        assertTrue(gridResponse[1][1] instanceof ShipSectionResponse);
    }

    @Test
    public void testMapNullEntity() {
        // Given
        BoardEntity entity = null;

        // When
        BoardEntityResponse entityResponse = mapper.map(entity);

        // Then
        assertNull(entityResponse);
    }

    @Test
    public void testMapShipSection() {
        // Given
        ShipSection shipSection = new ShipSection();
        shipSection.setHit(true);

        // When
        ShipSectionResponse actualShipSectionResponse = mapper.map(shipSection);

        // Then
        assertNotNull(actualShipSectionResponse);
        assertTrue(actualShipSectionResponse.isHit());

        ShipSectionResponse expectedShipSectionResponse = new ShipSectionResponse(true);
        assertEquals(expectedShipSectionResponse, actualShipSectionResponse);
    }

    @Test
    public void testMapInvalidEntity() {
        // Given
        BoardEntity entity = new BoardEntity() {
        };

        // Then
        assertThrows(IllegalArgumentException.class, () -> {
            // When
            mapper.map(entity);
        });
    }
}
