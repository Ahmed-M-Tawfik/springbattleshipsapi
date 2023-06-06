package com.altawfik.springbattleshipsapi.model.boardentity;

import com.altawfik.springbattleshipsapi.model.Ship;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@ToString(exclude = "parentShip")
@Data
public class ShipSection implements BoardEntity {
    private Ship parentShip;
    private boolean isHit;

    public ShipSection(final Ship parentShip) {
        this.parentShip = parentShip;
    }

    @JsonIgnore
    public Ship getParentShip() {
        return parentShip;
    }
}
