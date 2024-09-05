package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.states.State;

public abstract class ChoosingState implements State {
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;

    public ChoosingState() {
        setDefaultValueOfActivations();
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        this.isPullActive = false;
        this.isEndActive = false;
    }

    @Override
    public boolean isChooserActive() {
        return true;
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
