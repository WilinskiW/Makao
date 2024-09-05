package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.impl.*;

public class StateChecker {
    protected StateChecker() {}

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

    protected boolean isDefaultRescueState(Player player){
        return player.getState() instanceof DefaultRescueState;
    }
    protected boolean isDefenceRescueState(Player player){
        return player.getState() instanceof DefenceRescueState;
    }
    protected boolean isChoosingState(Player player){return player.getState() instanceof ChoosingState;}
    protected boolean isChoosingDemandState(Player player){return player.getState() instanceof ChoosingDemandState;}
    protected boolean isChoosingSuitState(Player player){return player.getState() instanceof ChoosingSuitState;}

}
