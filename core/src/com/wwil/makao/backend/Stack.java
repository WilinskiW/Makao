package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private final List<Card> cards = new ArrayList<>();

    public void addCardToStack(Card card){
        cards.add(card);
    }

    public Card peekCard(){
        return cards.get(getCards().size()-1);
    }
    public boolean isRefreshNeeded(){
        return getCards().size() > 3;
    }

    public List<Card> getCards() {
        return cards;
    }
}
