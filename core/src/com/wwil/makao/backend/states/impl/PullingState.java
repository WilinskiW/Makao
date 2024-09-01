package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.states.State;

public class PullingState extends PunishState {
    public PullingState(int amountOfPunishes) {
        super(amountOfPunishes);
    }

    public PullingState(int amountOfPunishes, boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        super(amountOfPunishes, isPutActive, isPullActive, isEndActive);
    }

    @Override
    public State saveState() {
        return new PullingState(amountOfPunishes, isPutActive, isPullActive, isEndActive);
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = false;
        this.isPullActive = true;
        this.isEndActive = false;
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
