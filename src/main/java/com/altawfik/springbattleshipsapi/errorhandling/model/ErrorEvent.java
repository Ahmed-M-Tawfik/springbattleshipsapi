package com.altawfik.springbattleshipsapi.errorhandling.model;

public record ErrorEvent(AppError error, Throwable throwable) {

    public AppError getError() {
        return error;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}