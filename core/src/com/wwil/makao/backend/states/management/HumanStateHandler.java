package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.states.impl.BlockedState;
import com.wwil.makao.backend.states.impl.PullingState;
import com.wwil.makao.backend.states.impl.PunishState;

public class HumanStateHandler {
    private final StateChanger changer;
    private final StateChecker checker;
    private final StateContext context;
    private final Human humanPlayer;

    public HumanStateHandler(StateChanger changer, StateChecker checker,StateContext context, Human humanPlayer) {
        this.changer = changer;
        this.checker = checker;
        this.context = context;
        this.humanPlayer = humanPlayer;
    }

    public void handleStateAfterPut(boolean isValid, int humanPlayedCards) {
        if (isValid) {
            setActions(true,false,true);
        } else if (checker.isRescueState(humanPlayer)) {
            changer.applyDefaultState(humanPlayer);
        } else if (humanPlayedCards == 0 && checker.isDefaultState(humanPlayer)) {
            setActions(true,true,false);
        }
    }

    public void handleStateAfterPull(boolean hasPullBefore) {
        if (checker.isDefenseState(humanPlayer)) {
            if (hasPullBefore) {
                changer.applyPunishment(humanPlayer);
            }
        } else if (checker.isDefaultState(humanPlayer)) {
            changer.applyDefaultRescueState(humanPlayer);
        } else if (checker.isPullingState(humanPlayer)) {
            handlePullingState();
        }
    }

    private void handlePullingState() {
        PunishState pullingState = (PullingState) context.getHumanState();
        pullingState.decreaseAmount();
        if (pullingState.getAmountOfPunishes() > 0) {
            setActions(false,true,false);
        } else {
            changer.applyDefaultState(humanPlayer);
            setActions(true,true,false);
        }
    }

    public void handleStateAfterEnd() {
        if (checker.isPlayerBlocked(humanPlayer)) {
            PunishState blockedState = (BlockedState) humanPlayer.getState();
            blockedState.decreaseAmount();
        }
        else if(checker.isRescueState(humanPlayer)){
            changer.applyDefaultState(humanPlayer);
        }
        setActions(false,false,false);
    }

    private void setActions(boolean put, boolean pull, boolean end) {
        context.activateActions(put, pull, end);
    }
}
