package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public interface StateContext {
    void changeState(Player player, State newState);
    void activateActions(Player player, boolean isPutActive, boolean isPullActive, boolean isEndActive, boolean isMakaoActive);
}
