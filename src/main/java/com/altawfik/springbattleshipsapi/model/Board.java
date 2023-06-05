package com.altawfik.springbattleshipsapi.model;

public record Board(BoardEntity[][] grid) {
    public Board(BoardSize boardSize) {
        this(new BoardEntity[boardSize.x()][boardSize.y()]);
    }
}
