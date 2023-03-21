package com.altawfik.springbattleshipsapi.api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ShipSectionResponse implements BoardEntityResponse {
    private final ShipResponse parentShip;
    private boolean isHit;

    public ShipSectionResponse(final ShipResponse parentShip) {
        this.parentShip = parentShip;
    }

    @JsonIgnore
    public ShipResponse getParentShip() {
        return parentShip;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit() {
        isHit = true;
    }
}
