package com.altawfik.springbattleshipsapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShipSection implements BoardEntity {
    private final Ship parentShip;
    private boolean isHit;

    public ShipSection(final Ship parentShip) {
        this.parentShip = parentShip;
    }

    @JsonIgnore
    public Ship getParentShip() {
        return parentShip;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit() {
        isHit = true;
    }
}