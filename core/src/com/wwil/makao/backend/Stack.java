package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private List<Card> cards = new ArrayList<>();

    public void addCardToStack(Card card){
        cards.add(card);
    }

    public boolean isRefreshNeeded(){
        return getCards().size() > 3;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
