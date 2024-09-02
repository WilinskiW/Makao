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

    public HumanStateHandler(StateChanger changer, StateChecker checker, StateContext context, Human humanPlayer) {
        this.changer = changer;
        this.checker = checker;
        this.context = context;
        this.humanPlayer = humanPlayer;
    }

    public void handleStateAfterPut(boolean isValid, int humanPlayedCards) {
        if (isValid) {
            if (checker.isDefaultRescueState(humanPlayer)) {
                changer.applyDefaultState(humanPlayer);
            }
            setActions(true, false, true);
        } else if (humanPlayedCards == 0 && checker.isDefaultState(humanPlayer)) {
            setActions(true, true, false);
        }
    }

    public void handleStateAfterPull(boolean hasPullBefore) {
        if (checker.isDefenseState(humanPlayer) || checker.isDefenceRescueState(humanPlayer)) {
            if (hasPullBefore) {
                changer.applyPunishment(humanPlayer);
            }
            else{
                changer.applyDefenceRescueState(humanPlayer);
            }
        }

        if (checker.isDefaultState(humanPlayer)) {
            changer.applyDefaultRescueState(humanPlayer);
        }

        if (checker.isPullingState(humanPlayer)) {
            handlePullingState();
        }
    }

    private void handlePullingState() {
        PunishState pullingState = (PullingState) context.getHumanState();
        pullingState.decreaseAmount();
        if (pullingState.getAmountOfPunishes() == 0) {
            changer.applyDefaultState(humanPlayer);
        }
    }

    public void handleStateAfterEnd() {
        if (checker.isPlayerBlocked(humanPlayer)) {
            PunishState blockedState = (BlockedState) humanPlayer.getState();
            blockedState.decreaseAmount();
        } else if (checker.isDefaultRescueState(humanPlayer)) {
            changer.applyDefaultState(humanPlayer);
        }
        setActions(false, false, false);
    }

    private void setActions(boolean put, boolean pull, boolean end) {
        context.activateActions(put, pull, end);
    }
}
