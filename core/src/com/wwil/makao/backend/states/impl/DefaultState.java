package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

import java.util.List;

public class DefaultState implements State {
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;

    public DefaultState() {
        setDefaultValueOfActivations();
    }

    public DefaultState(boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
    }

    @Override
    public State saveState() {
        return new DefaultState(isPutActive,isPullActive,isEndActive);
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        this.isPullActive = true;
        this.isEndActive = false;
    }

    @Override
    public List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardsForDefaultState(player, stackCard);
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForNormalTurn(chosenCard);
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
