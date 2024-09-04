package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public class ChoosingState implements State {
    @Override
    public State saveState() {
        return new ChoosingState();
    }

    @Override
    public void setDefaultValueOfActivations() {}

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForNormalTurn(chosenCard);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        Card card = cardFinder.findCardForChangeSuit(player, stackCard);
        card.setShadow(true);
        return card;
    }

    @Override
    public boolean isFocusDrawnCard() {
        return true;
    }

    @Override
    public boolean isPutActive() {
        return true;
    }

    @Override
    public void setPutActive(boolean putActive) {
    }

    @Override
    public boolean isPullActive() {
        return false;
    }

    @Override
    public void setPullActive(boolean pullActive) {
    }

    @Override
    public boolean isEndActive() {
        return false;
    }

    @Override
    public void setEndActive(boolean endActive) {
    }
}
