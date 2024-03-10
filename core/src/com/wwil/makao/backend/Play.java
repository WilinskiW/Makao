package com.wwil.makao.backend;

//Informacje jakie przekazuje frontend -> backend
public class Play {
    private final Card cardPlayed;
    private final boolean draw;
    private final boolean dropped;
    private final boolean chooserActive;

    public Play(Card cardPlayed, boolean draw, boolean dropped, boolean chooserActive) {
        this.cardPlayed = cardPlayed;
        this.draw = draw;
        this.dropped = dropped;
        this.chooserActive = chooserActive;
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

    public boolean isChooserActive() {
        return chooserActive;
    }
}
