package com.wwil.makao.backend.gameplay.actions;

import com.wwil.makao.backend.model.card.Card;

public class Play {
    private Card cardPlayed;
    private Card drawnCard;
    private Action action;

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

    public Card getDrawnCard() {
        return drawnCard;
    }

    public Play setDrawnCard(Card drawnCard) {
        this.drawnCard = drawnCard;
        return this;
    }
}
