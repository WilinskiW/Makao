package com.wwil.makao.backend.states.impl.rescue;

import com.wwil.makao.backend.states.State;

public abstract class RescueState implements State {
    protected boolean isPutActive;
    protected boolean isPullActive;
    protected boolean isEndActive;
    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        this.isPullActive = false;
        this.isEndActive = true;
    }

    @Override
    public boolean isFocusDrawnCard() {
        return true;
    }

    @Override
    public boolean isPutActive() {
        return isPutActive;
    }

    @Override
    public void setPutActive(boolean putActive) {
        this.isPutActive = putActive;
    }

    @Override
    public boolean isPullActive() {
        return isPullActive;
    }

    @Override
    public void setPullActive(boolean pullActive) {
        this.isPullActive = pullActive;
    }

    @Override
    public boolean isEndActive() {
        return isEndActive;
    }

    @Override
    public void setEndActive(boolean endActive) {
        this.isEndActive = endActive;
    }
}
