package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.CardFinder;

import java.util.ArrayList;
import java.util.List;

public class DefaultState extends PlayerState {
    @Override
    protected List<Card> findCards(CardFinder cardFinder, Card stackCard) {
        return cardFinder.findCardForDefaultState(player, stackCard);
    }

    @Override
    protected List<Play> failRescue(List<Play> plays) {
        return new ArrayList<>();
    }

    @Override
    public boolean isValid(Card chosenCard,CardValidator validator) {
        return validator.isValidForNormalTurn(chosenCard);
    }
}
