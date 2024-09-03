package com.wwil.makao.backend.core;

import com.wwil.makao.backend.model.card.Card;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private final List<Card> cards = new ArrayList<>();

    public void addCardToStack(Card card){
        cards.add(card);
    }

    protected Card peekCardBeforeLast(){
        return cards.get(getCards().size()-1);
    }
    protected Card peekCard(){
        return cards.get(getCards().size()-1);
    }

    protected List<Card> getCards() {
        return cards;
    }
}
