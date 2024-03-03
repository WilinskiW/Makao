package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;

import java.util.List;

public class PullDemander {
    private final int performerIndex;
    private final List<Card> cards;

    public PullDemander(int performerIndex, List<Card> cards) {
        this.performerIndex = performerIndex;
        this.cards = cards;
    }

    public int getPerformerIndex() {
        return performerIndex;
    }

    public List<Card> getCards() {
        return cards;
    }
}
