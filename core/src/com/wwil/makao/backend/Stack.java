package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;

import java.util.ArrayList;
import java.util.List;

public class Stack {
    private List<Card> cards = new ArrayList<>();

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

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
