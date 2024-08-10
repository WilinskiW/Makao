package com.wwil.makao.backend;

public class Play {
    private Card cardPlayed;
    private Action action;
    private Card cardFromChooser;

    public Play(Card cardPlayed, Card cardFromChooser) {
        this.cardPlayed = cardPlayed;
        this.cardFromChooser = cardFromChooser;
    }

    public Play() {
    }

    public boolean isDragging(){
        return action == Action.DRAG;
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

    Card getCardFromChooser() {
        return cardFromChooser;
    }
}
