package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;
//Informacje jakie przekazuje frontend -> backendowi
public class Play {
    private final Card cardPlayed;
    private final boolean draw;
    private final boolean dropped;

    public Play(Card cardPlayed, boolean draw, boolean dropped) {
        this.cardPlayed = cardPlayed;
        this.draw = draw;
        this.dropped = dropped;
    }

    public boolean isDropped() {
        return dropped;
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public boolean wantsToDraw() {
        return draw;
    }
}
