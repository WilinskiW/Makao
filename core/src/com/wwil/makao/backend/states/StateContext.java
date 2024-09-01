package com.wwil.makao.backend.states;

import com.wwil.makao.backend.model.player.Player;

public interface StateContext {
    void changeState(Player player,State newState);
    Player getPreviousPlayer();
    State getHumanState();
    Player getNextPlayer();
    void activateActions(boolean isPutActive, boolean isPullActive, boolean isEndActive);
}
