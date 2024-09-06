package com.wwil.makao.backend.states.impl.rescue;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

public class NormalRescueState extends RescueState {
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;

    public NormalRescueState() {
        setDefaultValueOfActivations();
    }

    public NormalRescueState(boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        this.isPutActive = isPutActive;
        this.isPullActive = isPullActive;
        this.isEndActive = isEndActive;
    }

    @Override
    public State saveState() {
        return new NormalRescueState(isPutActive, isPullActive, isEndActive);
    }

    @Override
    public void setDefaultValueOfActivations() {
        this.isPutActive = true;
        this.isPullActive = false;
        this.isEndActive = true;
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForNormalState(chosenCard);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findBestForNormalState(player, stackCard);
    }

    @Override
    public void handleEnd(Player player, StateChanger changer) {
        changer.applyNormalState(player);
        changer.setActions(player, false, false, false);
    }
}
