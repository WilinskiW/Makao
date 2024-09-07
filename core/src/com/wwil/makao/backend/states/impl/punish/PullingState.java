package com.wwil.makao.backend.states.impl.punish;

import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.base.PunishState;
import com.wwil.makao.backend.states.management.StateChanger;

public class PullingState extends PunishState {
    public PullingState(int amountOfPunishes) {
        super(amountOfPunishes);
        setDefaultValueOfActivations();
    }

    public PullingState(int amountOfPunishes, boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        super(amountOfPunishes, isPutActive, isPullActive, isEndActive);
    }

    @Override
    public State copyState() {
        return new PullingState(amountOfPunishes, isPutActive, isPullActive, isEndActive);
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = false;
        this.isPullActive = true;
        this.isEndActive = false;
    }

    @Override
    public void handlePull(Player player, StateChanger changer) {
        changer.handlePunishState(player, this);
    }

    @Override
    public boolean isPutActive() {
        return isPutActive;
    }

    @Override
    public void setPutActive(boolean putActive) {
        isPutActive = putActive;
    }

    @Override
    public boolean isPullActive() {
        return isPullActive;
    }

    @Override
    public void setPullActive(boolean pullActive) {
        isPullActive = pullActive;
    }

    @Override
    public boolean isEndActive() {
        return isEndActive;
    }

    @Override
    public void setEndActive(boolean endActive) {
        isEndActive = endActive;
    }
}
