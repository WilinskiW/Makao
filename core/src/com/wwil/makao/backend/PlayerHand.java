package com.wwil.makao.backend;

import java.util.List;

public class PlayerHand {
    private final List<Card> cards;

    public PlayerHand(List<Card> cards) {
        this.cards = cards;
    }

    public void addCardToHand(Card card){
        cards.add(card);
    }

    public void removeCardFromHand(Card card){
        cards.remove(card);
    }

    public List<Card> getCards() {
        return cards;
    }
}
