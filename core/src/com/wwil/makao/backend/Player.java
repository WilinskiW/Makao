package com.wwil.makao.backend;

import java.util.*;

public class Player {
    private final List<Card> cards;
    private boolean isAttack;

    public Player(List<Card> cards) {
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

    public boolean checkIfPlayerHaveNoCards() {
        return cards.isEmpty();
    }
    public boolean isAttack(){
        return isAttack;
    }

    void setAttack(boolean attack) {
        isAttack = attack;
    }

    public List<Card> getCards() {
        return cards;
    }
}
