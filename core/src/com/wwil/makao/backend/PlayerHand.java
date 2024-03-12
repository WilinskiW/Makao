package com.wwil.makao.backend;

import java.util.List;

public class PlayerHand {
    private final List<Card> cards;

    public PlayerHand(List<Card> cards) {
        this.cards = cards;
    }

    public void addCardToHand(Card card) {
        cards.add(card);
    }

    public void addCardsToHand(List<Card> newCards) {
        cards.addAll(newCards);
    }

    public void removeCardFromHand(Card card) {
        cards.remove(card);
    }

    public boolean checkIfPlayerHaveNoCards(){
        return cards.isEmpty();
    }

    public Card findDemandedCard(Card demanded){
        for(Card card : cards){
            if(card.getRank() == demanded.getRank()){
                return card;
            }
        }
        return null;
    }
    public Suit giveMostDominantSuit() {
        int[] counts = new int[4]; // Tablica przechowująca liczbę wystąpień dla każdego koloru

        for (Card card : cards) {
            counts[card.getSuit().ordinal()]++; // Inkrementacja odpowiedniego licznika
        }

        int maxIndex = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] > counts[maxIndex]) {
                maxIndex = i;
            }
        }

        return Suit.values()[maxIndex]; // Zwracanie koloru z największą liczbą wystąpień
    }
    public List<Card> getCards() {
        return cards;
    }
}
