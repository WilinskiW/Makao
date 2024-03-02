package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;
//Raport karty którą zagrał gracz lub dobrania
public class PlayReport {
    private PlayerHand player;
    private final Card played; //zagrana
    private final Card drawn;
    private boolean waiting;

    public boolean isWaiting() {
        return waiting;
    }

    public Card getDrawn() {
        return drawn;
    }

    public Card getPlayed() {
        return played;
    }

    public PlayReport(PlayerHand player, Card played, Card drawn) {
        this.player = player;
        this.played = played;
        this.drawn = drawn;
    }
}
