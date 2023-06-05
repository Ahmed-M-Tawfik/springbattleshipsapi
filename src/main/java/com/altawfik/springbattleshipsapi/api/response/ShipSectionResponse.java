package com.altawfik.springbattleshipsapi.api.response;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class ShipSectionResponse implements BoardEntityResponse {
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
