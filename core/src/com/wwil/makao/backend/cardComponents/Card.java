package com.wwil.makao.backend.cardComponents;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private boolean isDrawn = true; // ???

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void setDrawn(boolean drawn) {
        this.isDrawn = drawn;
    }

}
