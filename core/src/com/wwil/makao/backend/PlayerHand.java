package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;

import java.util.List;

public class PlayerHand {
    private final List<Card> cards;
    private boolean isWaiting = false;

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
    public List<Card> getCards() {
        return cards;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setWaiting(boolean waiting) {
        isWaiting = waiting;
    }
}
