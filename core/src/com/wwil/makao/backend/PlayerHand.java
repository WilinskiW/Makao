package com.wwil.makao.backend;

import java.util.List;

public class PlayerHand {
    private List<Card> cards;

    public PlayerHand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }
}
