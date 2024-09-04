package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.impl.BlockedState;
import com.wwil.makao.backend.states.impl.PullingState;
import com.wwil.makao.backend.states.impl.PunishState;

public class StateHandler {
    private final StateChanger changer;
    private final StateChecker checker;
    private final StateContext context;

    protected StateHandler(StateChanger changer, StateChecker checker, StateContext context) {
        this.changer = changer;
        this.checker = checker;
        this.context = context;
    }

    public void updateStateAfterPut(Player player, Card card) {
        if(checker.isChoosingState(player)){
            card.setShadow(true);
        }
        changer.applyDefaultState(player);
        setActions(true, false, true);
    }

    public void updateStateAfterPull(Player player, boolean hasPullBefore) {
        if (checker.isDefenseState(player) || checker.isDefenceRescueState(player)) {
            if (hasPullBefore) {
                changer.applyPunishment(player);
            } else {
                changer.applyDefenceRescueState(player);
            }
        }

        if (checker.isDefaultState(player)) {
            changer.applyDefaultRescueState(player);
        }

        if (checker.isPullingState(player)) {
            handlePullingState(player);
        }
    }

    private void handlePullingState(Player player) {
        PunishState pullingState = (PullingState) context.getPlayerState();
        pullingState.decreaseAmount();
        if (pullingState.getAmountOfPunishes() == 0) {
            changer.applyDefaultState(player);
        }
    }

    public void updateStateAfterEnd(Player player) {
        if (checker.isPlayerBlocked(player)) {
            PunishState blockedState = (BlockedState) player.getState();
            blockedState.decreaseAmount();
        } else if (checker.isDefaultRescueState(player)) {
            changer.applyDefaultState(player);
        }
        setActions(false, false, false);
    }

    private void setActions(boolean put, boolean pull, boolean end) {
        context.activateActions(put, pull, end);
    }
}
