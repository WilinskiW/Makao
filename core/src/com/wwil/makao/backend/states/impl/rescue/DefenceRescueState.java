package com.wwil.makao.backend.states.impl.rescue;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

public class DefenceRescueState extends RescueState {
    private final boolean isAttackByFour;

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
        return validator.isValidForDefenceState(chosenCard);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findBestCardForDefenceState(player);
    }

    @Override
    public void handlePull(Player player, StateChanger changer) {
        changer.applyPunishment(player);
    }

    @Override
    public void handleEnd(Player player, StateChanger changer) {
        changer.applyNormalState(player);
        changer.setActions(player, false, false, false);
    }

}
