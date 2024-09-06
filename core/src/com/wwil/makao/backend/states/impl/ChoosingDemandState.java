package com.wwil.makao.backend.states.impl;

import com.wwil.makao.backend.gameplay.CardFinder;
import com.wwil.makao.backend.gameplay.CardValidator;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public class ChoosingDemandState extends ChoosingState{
    @Override
    public State saveState() {
        return new ChoosingDemandState();
    }

    @Override
    public Card findValidCard(CardFinder cardFinder, Player player, Card stackCard) {
        return cardFinder.findCardForDemand(player, stackCard);
    }
}
