package com.altawfik.springbattleshipsapi.model;

import com.altawfik.springbattleshipsapi.model.boardentity.BoardEntity;

public record Board(BoardEntity[][] grid) {
    public Board(BoardSize boardSize) {
        this(new BoardEntity[boardSize.x()][boardSize.y()]);
    }
}
