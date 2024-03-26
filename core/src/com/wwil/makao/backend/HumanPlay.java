package com.wwil.makao.backend;

import java.util.List;

public class HumanPlay extends Play{
    private final boolean chooserActive;
    private final boolean endTurn;
    public HumanPlay(List<Card> cardsPlayed, boolean draw, boolean dropped, boolean chooserActive, boolean skipTurn, boolean demanding, boolean endTurn) {
        super(cardsPlayed, draw, dropped, skipTurn, demanding);
        this.chooserActive = chooserActive;
        this.endTurn = endTurn;
    }

    public boolean isChooserActive() {
        return chooserActive;
    }

    public boolean wantToEndTurn() {
        return endTurn;
    }
}
