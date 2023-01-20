package com.altawfik.springbattleshipsapi.model;

public class ShipSection implements BoardEntity {
    private final Ship parentShip;
    private boolean isHit;

    public ShipSection(final Ship parentShip) {
        this.parentShip = parentShip;
    }

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
