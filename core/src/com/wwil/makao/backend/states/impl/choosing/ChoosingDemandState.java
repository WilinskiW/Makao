package com.wwil.makao.backend.states.impl.choosing;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateChanger;

public class ChoosingDemandState extends ChoosingState{
    @Override
    public State saveState() {
        return new ChoosingDemandState();
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardForDemand(player, stackCard);
    }

    @Override
    public void handlePut(Player player, Card card, StateChanger changer) {
        if(card.isShadow()) {
            changer.applyAllDefenceState(card);
            changer.setActions(player, false, false, true);
        }
    }

}