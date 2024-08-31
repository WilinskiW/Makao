package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public class DefaultState extends PlayerState {

    public DefaultState(Player player) {
        super(player);
    }

    @Override
    void setDefaultValueOfActivations() {
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
}
