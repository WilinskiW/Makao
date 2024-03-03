package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;
//Raport karty którą zagrał gracz lub dobrania
public class PlayReport {
    private final PlayerHand player;
    private final Play play;
    private final Card drawn;
    private final boolean isCardCorrect;
    private boolean waiting;

    public PlayReport(PlayerHand player, Play play, Card drawn, boolean isCardCorrect) {
        this.player = player;
        this.play = play;
        this.drawn = drawn;
        this.isCardCorrect = isCardCorrect;
    }

    public Card getDrawn() {
        return drawn;
    }

    public Play getPlay() {
        return play;
    }

    public boolean isCardCorrect() {
        return isCardCorrect;
    }

    public boolean isWaiting() {
        return waiting;
    }
}
