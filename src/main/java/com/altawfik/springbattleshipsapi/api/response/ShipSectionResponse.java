package com.altawfik.springbattleshipsapi.api.response;

public class ShipSectionResponse implements BoardEntityResponse {
    //private final ShipResponse parentShip;
    private boolean isHit;

    public ShipSectionResponse(boolean isHit) {
        this.isHit = isHit;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit() {
        isHit = true;
    }
}
