package com.wwil.makao.backend.gameplay.actions;

import com.wwil.makao.backend.model.card.Card;

public class Play {
    private Card cardPlayed;
    private Card drawnCard;
    private ActionType actionType;

    public Play() {
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public ActionType getAction() {
        return actionType;
    }

    public Play setAction(ActionType actionType) {
        this.actionType = actionType;
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
