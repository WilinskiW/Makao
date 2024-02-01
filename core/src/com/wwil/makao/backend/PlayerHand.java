package com.wwil.makao.backend;

import java.util.List;

public class PlayerHand {
    private List<Card> cards;
    private boolean noCards = false;

    public PlayerHand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isNoCards() {
        return noCards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
