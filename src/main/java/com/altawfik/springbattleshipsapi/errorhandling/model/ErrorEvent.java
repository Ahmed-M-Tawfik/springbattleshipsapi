package com.altawfik.springbattleshipsapi.errorhandling.model;

public record ErrorEvent(AppError error, Throwable throwable) {
}