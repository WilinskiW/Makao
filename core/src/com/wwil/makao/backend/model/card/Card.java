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

    public boolean matchesRank(Rank rank){
        return this.rank == rank;
    }

    public boolean matchesSuit(Card other){
        return this.suit == other.suit;
    }
    public boolean canActiveChooser(Card card){
        return card.matchesRank(Rank.J) || card.matchesRank(Rank.AS);
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

    public Card setShadow(boolean shadow) {
        isShadow = shadow;
        return this;
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank=" + rank +
                ", suit=" + suit +
                '}';
    }
}
