package com.wwil.makao.backend.states.impl.choosing;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

public class ChoosingCardState extends ChoosingState{
    @Override
    public State saveState() {
        return new ChoosingCardState();
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return null;
    }

    @Override
    public void handlePut(Player player, Card card, StateChanger changer) {
        if(card.isShadow()){
            super.handlePut(player,card,changer);
        }
    }
}
