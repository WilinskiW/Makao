package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.impl.*;

public class StateChecker {
    public boolean isPlayerBlocked(Player player) {
        return player.getState() instanceof BlockedState;
    }

    protected boolean isPullingState(Player player) {
        return player.getState() instanceof PullingState;
    }

    public boolean isDefenseState(Player player) {
        return player.getState() instanceof DefenseState;
    }

    protected boolean isDefaultState(Player player) {
        return player.getState() instanceof DefaultState;
    }

    protected boolean isRescueState(Player player){
        return player.getState() instanceof DefaultRescueState;
    }
}