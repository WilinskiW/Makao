package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;
import com.wwil.makao.backend.states.State;

import java.util.List;

public class StateManager implements StateContext {
    private final PlayerManager playerManager;
    private final StateChanger stateChanger;
    private final StateHandler stateHandler;

    public StateManager(RoundManager roundManager, PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.stateChanger = new StateChanger(roundManager, this);
        this.stateHandler = new StateHandler(stateChanger, new StateChecker(), this);
    }

    public void activateActions(boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        getPlayerState().setPutActive(isPutActive);
        getPlayerState().setPullActive(isPullActive);
        getPlayerState().setEndActive(isEndActive);
    }

    @Override
    public void changeState(Player player, State newState) {
        player.changeState(newState);
    }
    @Override
    public State getPlayerState() {
        return playerManager.getCurrentPlayer().getState();
    }

    public StateChanger getStateChanger() {
        return stateChanger;
    }

    public StateHandler getStateHandler() {
        return stateHandler;
    }
}

