package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;
//Raport karty którą zagrał gracz lub dobrania
public class PlayReport {
    private PlayerHand player;
    private Card played; //zagrana
    private Card drawn;
    private boolean waiting;

    public boolean isWaiting() {
        return waiting;
    }

    public Card getPlayed() {
        return played;
    }

    public PlayReport(PlayerHand player, Card played) {
        this.player = player;
        this.played = played;
    }
}
