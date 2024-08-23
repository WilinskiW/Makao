package com.wwil.makao.backend.model.player;

import com.wwil.makao.backend.model.card.Card;

import java.util.List;

public class Human extends Player{
    public Human(List<Card> cards) {
        super(cards);
    }

    @Override
    public boolean isHuman() {
        return true;
    }
}
