package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class Play {
    private Card cardPlayed;
    private Action action;
    private List<Card> pullDeck = new ArrayList<>();
    private Card cardFromChooser;

    public Play(Card cardPlayed, Card cardFromChooser) {
        this.cardPlayed = cardPlayed;
        this.cardFromChooser = cardFromChooser;
    }

    public Play() {
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }


    public Action getAction() {
        return action;
    }

    public Play setAction(Action action) {
        this.action = action;
        return this;
    }

    public Play setCardPlayed(Card cardPlayed) {
        this.cardPlayed = cardPlayed;
        return this;
    }

    public Play setCardFromChooser(Card cardFromChooser) {
        this.cardFromChooser = cardFromChooser;
        return this;
    }

    List<Card> getPullDeck() {
        return pullDeck;
    }

    Play setPullDeck(List<Card> pullDeck) {
        this.pullDeck = pullDeck;
        return this;
    }

    Card getCardFromChooser() {
        return cardFromChooser;
    }
}
