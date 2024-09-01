package com.wwil.makao.backend.states.impl;


import com.wwil.makao.backend.states.State;

public class BlockedState extends PunishState {

    public BlockedState(int amountOfPunishes) {
        super(amountOfPunishes);
        setDefaultValueOfActivations();
    }

    public BlockedState(int amountOfPunishes, boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        super(amountOfPunishes, isPutActive, isPullActive, isEndActive);
    }

    @Override
    public State saveState() {
        return new BlockedState(amountOfPunishes, isPutActive, isPullActive, isEndActive);
    }

    public boolean canUnblock() {
        return amountOfPunishes == 0;
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = false;
        this.isPullActive = false;
        this.isEndActive = true;
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
