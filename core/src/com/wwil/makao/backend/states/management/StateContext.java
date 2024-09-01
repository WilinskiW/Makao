package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public interface StateContext {
    void changeState(Player player, State newState);
    Player getPreviousPlayer();
    State getHumanState();
    Player getNextPlayer();
    void activateActions(boolean isPutActive, boolean isPullActive, boolean isEndActive);
}
