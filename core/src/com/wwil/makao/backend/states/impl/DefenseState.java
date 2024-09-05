package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public class DefenseState implements State {
    private final Card attackingCard;
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;

    public DefenseState(Card attackingCard) {
        this.attackingCard = attackingCard;
        setDefaultValueOfActivations();
    }

    public DefenseState(Card attackingCard, boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        this.attackingCard = attackingCard;
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
    }

    @Override
    public State saveState() {
        return new DefenseState(attackingCard, isPutActive, isPullActive, isEndActive);
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        if(attackingCard.getRank() == Rank.FOUR) {
            this.isPullActive = false;
            this.isEndActive = true;
        }
        else{
            this.isPullActive = true;
            this.isEndActive = false;
        }
    }

    public Card getAttackingCard() {
        return attackingCard;
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findBestCardForDefenceState(player);
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForDefenceState(chosenCard);
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
