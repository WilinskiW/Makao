package com.wwil.makao.backend.model.player;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.states.DefaultState;
import com.wwil.makao.backend.states.PlayerState;

import java.util.*;

public abstract class Player {
    private final List<Card> cards;
    private PlayerState state;

    public Player(List<Card> cards) {
        this.cards = cards;
        this.state = new DefaultState(this);
    }

    public abstract boolean isHuman();

    public void addCardToHand(Card card) {
        cards.add(card);
    }

    public void removeCardFromHand(Card card) {
        cards.remove(card);
    }

    public boolean checkIfPlayerHaveNoCards() {
        return cards.isEmpty();
    }
    public void changeState(PlayerState state){
        this.state = state;
    }
    public PlayerState getState(){
        return state;
    }
    public List<Card> getCards() {
        return cards;
    }
}
