package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

import java.util.List;

public class DefenceRescueState implements State {
    private final boolean isAttackByFour;
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;

    public DefenceRescueState(boolean isAttackByFour) {
        this.isAttackByFour = isAttackByFour;
        setDefaultValueOfActivations();
    }

    public DefenceRescueState(boolean isAttackByFour, boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        this.isAttackByFour = isAttackByFour;
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
    }

    @Override
    public State saveState() {
        return new DefenceRescueState(isAttackByFour, isPutActive, isPullActive, isEndActive);
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        if (isAttackByFour) {
            this.isPullActive = false;
            this.isEndActive = true;
        } else {
            this.isPullActive = true;
            this.isEndActive = false;
        }
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForDefence(chosenCard);
    }

    @Override
    public List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardsForDefenceState(player, stackCard);
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
