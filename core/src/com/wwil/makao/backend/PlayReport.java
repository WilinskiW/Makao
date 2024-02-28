package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;

public class PlayReport {
    private PlayerHand player;
    private Card played;
    private Card drawn;


    public PlayReport(PlayerHand player, Card played) {
        this.player = player;
        this.played = played;
    }
}
