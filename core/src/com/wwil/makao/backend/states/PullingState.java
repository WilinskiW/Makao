package com.wwil.makao.backend.states;

public class PullingState extends PunishState{
    protected PullingState(int amountOfPunishes) {
        super(amountOfPunishes);
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
