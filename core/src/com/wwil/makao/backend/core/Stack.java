package com.wwil.makao.backend.core;

import com.wwil.makao.backend.model.card.Card;

import java.util.LinkedList;

public class Stack {
    private final LinkedList<Card> cards;

    protected Stack() {
        this.cards = new LinkedList<>();
    }

    protected void addCardToStack(Card card){
        cards.add(card);
    }

    protected Card pollLast(){
        return cards.pollLast();
    }
    protected Card peekCard(){
        return cards.getLast();
    }

    protected LinkedList<Card> getCards() {
        return cards;
    }
}
