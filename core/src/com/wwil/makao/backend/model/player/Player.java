package com.wwil.makao.backend.model.player;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.states.impl.NormalState;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.management.StateHandler;

import java.util.*;

public abstract class Player {
    private final int id;
    private final List<Card> cards;
    private State state;

    public Player(int id, List<Card> cards) {
        this.id = id;
        this.cards = cards;
        this.state = new NormalState();
    }

    public abstract boolean isHuman();
    public abstract void handleMakaoAction(Human humanPlayer, StateHandler  stateHandler);
    public boolean hasOneCard() {
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

    public void changeState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Player " + (id+1);
    }
}
