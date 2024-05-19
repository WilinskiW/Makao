package com.wwil.makao.backend;

import java.util.List;

public class Play {
    private List<Card> cardsPlayed;
    private Action action;
    private boolean blocked;
    private boolean demanding;
    private Card cardFromChooser;

    public Play(List<Card> cardsPlayed, boolean blocked, boolean demanding, Card cardFromChooser) {
        this.cardsPlayed = cardsPlayed;
        this.blocked = blocked;
        this.demanding = demanding;
        this.cardFromChooser = cardFromChooser;
    }

    public Play() {
    }

    public boolean isDemanding() {
        return demanding;
    }

    public boolean isBlock() {
        return blocked;
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public Card getCardPlayed() {
        if (cardsPlayed != null) {
            return cardsPlayed.get(0);
        }
        return null;
    }

    public Action getAction() {
        return action;
    }

    public Play setAction(Action action) {
        this.action = action;
        return this;
    }

    public Play setCardsPlayed(List<Card> cardsPlayed) {
        this.cardsPlayed = cardsPlayed;
        return this;
    }

    public Play setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    public Play setDemanding(boolean demanding) {
        this.demanding = demanding;
        return this;
    }


    public Play setCardFromChooser(Card cardFromChooser) {
        this.cardFromChooser = cardFromChooser;
        return this;
    }
}
