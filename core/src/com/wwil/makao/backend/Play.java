package com.wwil.makao.backend;

import java.util.List;

public abstract class Play {
    private final List<Card> cardsPlayed;
    private final boolean draw;
    private final boolean dropped;
    private final boolean blocked;
    private final boolean demanding;

    public Play(List<Card> cardsPlayed, boolean draw, boolean dropped, boolean blocked, boolean demanding) {
        this.cardsPlayed = cardsPlayed;
        this.draw = draw;
        this.dropped = dropped;
        this.blocked = blocked;
        this.demanding = demanding;
    }

    public boolean isDemanding() {
        return demanding;
    }

    public boolean isBlock() {
        return blocked;
    }

    public boolean isNotDropped() {
        return !dropped;
    }

    public List<Card> getCardsPlayed() {
        return cardsPlayed;
    }

    public Card getCardPlayed(){
        if(cardsPlayed.get(0) != null){
            return cardsPlayed.get(0);
        }
        return null;
    }

    public boolean wantsToDraw() {
        return draw;
    }
}
