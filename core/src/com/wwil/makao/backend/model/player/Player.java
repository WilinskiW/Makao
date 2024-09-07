package com.wwil.makao.backend.model.player;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.states.impl.NormalState;
import com.wwil.makao.backend.states.State;

import java.util.*;

public abstract class Player {
    private final List<Card> cards;
    private State state;

    public Player(List<Card> cards) {
        this.cards = cards;
        this.state = new NormalState();
    }
    public abstract boolean isHuman();
    public boolean hasOneCard(){
        return cards.size() == 1;
    }

    public void addCardToHand(Card card) {
        cards.add(card);
    }

    public void removeCardFromHand(Card card) {
        cards.remove(card);
    }

    public boolean checkIfPlayerHaveNoCards() {
        return cards.isEmpty();
    }
    public void changeState(State state){
        this.state = state;
    }
    public State getState(){
        return state;
    }
    public List<Card> getCards() {
        return cards;
    }
}
