package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.validation.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.utils.CardFinder;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

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
    public State copyState() {
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
        return cardFinder.findCardForDefenceState(player);
    }

    @Override
    public void handlePull(Player player, StateChanger changer) {
        changer.applyRescueState(player);
    }

    @Override
    public void handleEnd(Player player, StateChanger changer) {
        changer.applyPunishment(player);
        State.super.handleEnd(player, changer);
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

    @Override
    public String toString() {
        return " is attack by " + attackingCard;
    }
}
