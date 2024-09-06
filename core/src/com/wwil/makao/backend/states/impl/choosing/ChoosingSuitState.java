package com.wwil.makao.backend.states.impl.choosing;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

public class ChoosingSuitState extends ChoosingState {

    @Override
    public State saveState() {
        return new ChoosingSuitState();
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardForChangeSuit(player, stackCard);
    }

    @Override
    public void handlePut(Player player, Card card, StateChanger changer) {
        if (card.isShadow()) {
            super.handlePut(player, card, changer);
        }
    }
}