package com.wwil.makao.backend;

public class Play {
    private final Card cardPlayed;
    private final boolean draw;
    private final boolean dropped;
    private final boolean chooserActive;
    private final boolean skipTurn;

    public Play(Card cardPlayed, boolean draw, boolean dropped, boolean chooserActive, boolean skipTurn) {
        this.cardPlayed = cardPlayed;
        this.draw = draw;
        this.dropped = dropped;
        this.chooserActive = chooserActive;
        this.skipTurn = skipTurn;
    }

    public boolean isDemanding(){
        return isChooserActive() && isNotDropped();
    }

    public boolean isBlock() {
        return skipTurn;
    }

    public boolean isNotDropped() {
        return !dropped;
    }

    public Card getCardPlayed() {
        return cardPlayed;
    }

    public boolean wantsToDraw() {
        return draw;
    }

    public boolean isChooserActive() {
        return chooserActive;
    }
}
