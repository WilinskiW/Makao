package com.wwil.makao.backend.core;

import com.wwil.makao.backend.model.card.Card;

import java.util.LinkedList;
import java.util.List;

public class Stack {
    private final LinkedList<Card> cards;

    public Stack() {
        this.cards = new LinkedList<>();
    }

    public void addCardToStack(Card card){
        cards.add(card);
    }

    protected Card peekDemandCard(){
        if(cards.size() > 1){
            int indexOfLast = cards.indexOf(cards.getLast());
            return cards.get(indexOfLast-1);
        }
        return peekCard();
    }
    protected Card peekCard(){
        return cards.getLast();
    }

    protected List<Card> getCards() {
        return cards;
    }
}
