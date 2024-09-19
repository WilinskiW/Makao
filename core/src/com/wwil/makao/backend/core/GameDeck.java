package com.wwil.makao.backend.core;

import com.wwil.makao.backend.model.card.Card;

import java.util.LinkedList;

public class GameDeck {
    private final LinkedList<Card> cards;

    public GameDeck(LinkedList<Card> cards) {
        this.cards = cards;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }
}
