package com.altawfik.springbattleshipsapi.api.mapper;

import com.altawfik.springbattleshipsapi.api.response.BattleResponse;
import com.altawfik.springbattleshipsapi.api.response.BoardEntityResponse;
import com.altawfik.springbattleshipsapi.api.response.MissResponse;
import com.altawfik.springbattleshipsapi.api.response.ShipSectionResponse;
import com.altawfik.springbattleshipsapi.model.Battle;
import com.altawfik.springbattleshipsapi.model.boardentity.BoardEntity;
import com.altawfik.springbattleshipsapi.model.boardentity.Miss;
import com.altawfik.springbattleshipsapi.model.boardentity.ShipSection;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BattleToBattleResponseMapper {
    BattleResponse map(Battle battle);

    default BoardEntityResponse[][] map(BoardEntity[][] grid) {
        BoardEntityResponse[][] gridResponse = new BoardEntityResponse[grid.length][grid[0].length];
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[0].length; j++) {
                gridResponse[i][j] = map(grid[i][j]);
            }
        }

        return gridResponse;
    }

    default BoardEntityResponse map(BoardEntity entity) {
        if(entity == null) {
            return null;
        }
        if(entity instanceof ShipSection) {
            return map((ShipSection) entity);
        }
        if(entity instanceof Miss) {
            return new MissResponse();
        }
        throw new IllegalArgumentException();
    }

    default ShipSectionResponse map(ShipSection shipSection) {
        return new ShipSectionResponse(shipSection.isHit());
    }
}
