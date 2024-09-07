package com.wwil.makao.backend.states.impl.choosing;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

public class ChoosingCardState extends ChoosingState {
    private final State previousState;

    public ChoosingCardState(State previousState) {
        this.previousState = previousState;
    }

    @Override
    public State copyState() {
        return new ChoosingCardState(previousState);
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return previousState.findValidCard(cardFinder, player, stackCard);
    }

    @Override
    public boolean isValid(Card chosenCard, CardValidator validator) {
        return previousState.isValid(chosenCard, validator);
    }

    @Override
    public void handlePut(Player player, Card card, StateChanger changer) {
        if (card.isShadow()) {
            super.handlePut(player, card, changer);
        }
    }
}
