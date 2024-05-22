package com.wwil.makao.backend;

public class Card {
    private final Rank rank;
    private final Suit suit;
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public boolean isBattleCard(){
        return rank == Rank.TWO
                || rank == Rank.THREE
                || (rank == Rank.K && suit == Suit.HEART)
                || (rank == Rank.K && suit == Suit.SPADE);
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "Card{" +
                "rank=" + rank +
                ", suit=" + suit +
                '}';
    }
}
