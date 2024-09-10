package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.gameplay.management.RoundManager;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.gameplay.management.PlayerManager;
import com.wwil.makao.backend.states.State;

public class PlayerStateManager implements StateContext {
    private final PlayerManager playerManager;
    private final StateChanger stateChanger;
    private final StateHandler stateHandler;

    public PlayerStateManager(RoundManager roundManager, PlayerManager playerManager) {
        this.playerManager = playerManager;
        this.stateChanger = new StateChanger(roundManager, this);
        this.stateHandler = new StateHandler(stateChanger);
    }

    public void activateActions(Player player, boolean isPutActive, boolean isPullActive, boolean isEndActive, boolean isMakaoActive) {
        State state = player.getState();
        state.setPutActive(isPutActive);
        state.setPullActive(isPullActive);
        state.setEndActive(isEndActive);
        state.setMakaoActive(isMakaoActive);
    }

    public void resetAllActionsActivation(){
        playerManager.getPlayers().forEach(player -> player.getState().setDefaultValueOfActivations());
    }

    @Override
    public void changeState(Player player, State newState) {
        player.changeState(newState);
    }
    public StateChanger getStateChanger() {
        return stateChanger;
    }

    public StateHandler getStateHandler() {
        return stateHandler;
    }
}

