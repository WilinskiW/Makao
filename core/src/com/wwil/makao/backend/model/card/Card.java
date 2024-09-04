package com.wwil.makao.backend.model.card;

public class Card {
    private final Rank rank;
    private final Suit suit;
    private boolean isShadow;
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public boolean matchesRank(Card other){
        return this.rank == other.getRank();
    }

    public boolean matchesSuit(Card other){
        return this.suit == other.suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean isShadow() {
        return isShadow;
    }

    public void setShadow(boolean shadow) {
        isShadow = shadow;
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank=" + rank +
                ", suit=" + suit +
                '}';
    }
}
