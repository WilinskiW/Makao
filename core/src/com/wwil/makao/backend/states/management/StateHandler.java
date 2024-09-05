package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.model.card.Ability;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.impl.*;

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
        if (checker.isChoosingDemandState(player) && card.isShadow()) {
            changer.applyAllDefenceState(card);
            setActions(player,false, false, true);
        } else if (checker.isChoosingSuitState(player) && card.isShadow()) {
            changer.applyDefaultState(player);
            setActions(player,true, false, true);
        } else if (!checker.isChoosingState(player)) {
            changer.applyDefaultState(player);
            setActions(player,true, false, true);
        }
    }

    public void updateStateAfterPull(Player player) {
        if (checker.isDefenseState(player)) {
            applyRescueState(player);
        } else if (checker.isDefenceRescueState(player)) {
            changer.applyPunishment(player);
        } else if (checker.isDefaultState(player)) {
            changer.applyDefaultRescueState(player);
        }  else if (checker.isPullingState(player)) {
            changer.handlePullingState(player);
        } else {
            changer.applyDefaultState(player);
            setActions(player,false, false, true);
        }
    }

    public void applyRescueState(Player player) {
        DefenseState defenseState = (DefenseState) player.getState();
        if (defenseState.getAttackingCard().getRank().getAbility() == Ability.NONE) {
            changer.applyDemandRescueState(player);
        } else {
            changer.applyDefenceRescueState(player);
        }
    }


    public void updateStateAfterEnd(Player player) {
        if (checker.isPlayerBlocked(player)) {
            PunishState blockedState = (BlockedState) player.getState();
            blockedState.decreaseAmount();
        } else if (checker.isDefaultRescueState(player)) {
            changer.applyDefaultState(player);
        }
        setActions(player,false, false, false);
    }

    private void setActions(Player player, boolean put, boolean pull, boolean end) {
        context.activateActions(player, put, pull, end);
    }
}
