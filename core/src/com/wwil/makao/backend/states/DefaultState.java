package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.PlayReport;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;
import com.wwil.makao.backend.model.player.Player;

import java.util.List;

public class DefaultState extends PlayerState {

    @Override
    protected List<Card> findValidCards(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardsForDefaultState(player, stackCard);
    }

    @Override
    PlayReport setActionActivations(PlayReport playReport) {
        return playReport.setPutActive().setPullActive();
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return validator.isValidForNormalTurn(chosenCard);
    }
}
