package com.wwil.makao.backend;

import java.util.List;

public class HumanPlay extends Play{
    private final boolean chooserActive;
    private final boolean endTurn = false;
    public HumanPlay(List<Card> cardsPlayed, boolean draw, boolean dropped, boolean chooserActive, boolean skipTurn, boolean demanding) {
        super(cardsPlayed, draw, dropped, skipTurn, demanding);
        this.chooserActive = chooserActive;
    }

    public boolean isChooserActive() {
        return chooserActive;
    }

    public boolean wantToEndTurn() {
        return endTurn;
    }
}
