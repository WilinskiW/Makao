package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.validation.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.utils.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

public class NormalState implements State {
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;
    private boolean isMakaoActive;

    public NormalState() {
        setDefaultValueOfActivations();
    }

    public NormalState(boolean isPutActive, boolean isPullActive, boolean isEndActive, boolean isMakaoActive) {
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
        this.isMakaoActive = isMakaoActive;
    }

    @Override
    public State copyState() {
        return new NormalState(isPutActive, isPullActive, isEndActive, isMakaoActive);
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        this.isPullActive = true;
        this.isEndActive = false;
        this.isMakaoActive = true;
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findForNormalState(player, stackCard);
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForNormalState(chosenCard);
    }

    @Override
    public void handlePut(Player player, Card card, StateChanger changer) {
        changer.applyNormalState(player);
        changer.setActions(player, true, false, true, true);
    }

    @Override
    public void handlePull(Player player, StateChanger changer) {
        changer.applyNormalRescueState(player);
    }

    @Override
    public void handleEnd(Player player, StateChanger changer) {
        changer.applyNormalState(player);
        State.super.handleEnd(player, changer);
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

    @Override
    public boolean isMakaoActive() {
        return isMakaoActive;
    }

    @Override
    public void setMakaoActive(boolean makaoActive) {
        isMakaoActive = makaoActive;
    }
}
