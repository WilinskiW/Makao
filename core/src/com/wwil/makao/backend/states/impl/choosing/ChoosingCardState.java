package com.wwil.makao.backend.states.impl.choosing;

import com.wwil.makao.backend.gameplay.utils.CardFinder;
import com.wwil.makao.backend.gameplay.validation.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.base.ChoosingState;
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
        return cardFinder.getCardChooser().chooseCardForWildCard(previousState);
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

    @Override
    public String toString() {
        return " chose card: ";
    }
}
