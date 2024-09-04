package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public class ChoosingState implements State {
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;
    @Override
    public State saveState() {
        return new ChoosingState();
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        this.isPullActive = true;
        this.isEndActive = false;
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForDefaultState(chosenCard);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardForChangeSuit(player, stackCard);
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
