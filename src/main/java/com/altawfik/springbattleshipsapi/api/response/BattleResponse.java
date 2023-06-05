package com.altawfik.springbattleshipsapi.api.response;

import com.altawfik.springbattleshipsapi.api.BaseResponse;
import com.altawfik.springbattleshipsapi.model.BattleState;
import com.altawfik.springbattleshipsapi.model.Player;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
public class BattleResponse extends BaseResponse {
    private final PlayerResponse[] players;
    private final BoardResponse[] boards;
    private final BoardSizeResponse boardSize;
    private final BattleStateResponse state;

    public PlayerResponse[] getPlayers() {
        return players;
    }

    public BattleStateResponse getState() {
        return state;
    }
}
