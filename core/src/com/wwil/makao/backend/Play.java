package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;

public class Play {
    private Card cardPlayed;
    private boolean draw;

    public Play(Card cardPlayed, boolean draw) {
        this.cardPlayed = cardPlayed;
        this.draw = draw;
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public boolean wantsToDraw() {
        return draw;
    }
}
