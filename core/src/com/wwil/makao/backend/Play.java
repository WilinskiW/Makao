package com.wwil.makao.backend;

public class Play {
    private Card cardPlayed;
    private Card drawnCard;
    private Action action;
    private boolean isChooserActive;

    public Play(Card cardPlayed) {
        this.cardPlayed = cardPlayed;
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

    public Card getDrawnCard() {
        return drawnCard;
    }

    public Play setDrawnCard(Card drawnCard) {
        this.drawnCard = drawnCard;
        return this;
    }

    public boolean isChooserActive() {
        return isChooserActive;
    }

    public Play setChooserActive() {
        isChooserActive = true;
        return this;
    }
}
