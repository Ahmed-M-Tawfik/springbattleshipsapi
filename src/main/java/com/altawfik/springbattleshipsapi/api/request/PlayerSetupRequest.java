package com.altawfik.springbattleshipsapi.api.request;

import javax.validation.constraints.NotBlank;

public record PlayerSetupRequest(PlayerNumber playerNumber, @NotBlank /*doesn't work for records in Spring Boot 5*/ String playerName) {
}
