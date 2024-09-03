package com.wwil.makao.backend.model.card;

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
                || isKingSpade();
    }

    public boolean matchesRank(Card other){
        return this.rank == other.getRank();
    }

    public boolean matchesSuit(Card other){
        return this.suit == other.suit;
    }

    public boolean isKingSpade(){
        return rank == Rank.K && suit == Suit.SPADE;
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
