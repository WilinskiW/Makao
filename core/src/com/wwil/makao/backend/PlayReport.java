package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;
//Raport karty którą zagrał gracz lub dobrania
public class PlayReport {
    private final PlayerHand playerHand;
    private final PullDemander pullDemander;
    private final Play play;
    private final Card drawn;
    private final boolean isCardCorrect;
    private boolean waiting;

    public PlayReport(PlayerHand playerHand, PullDemander pullDemander, Play play, Card drawn, boolean isCardCorrect) {
        this.playerHand = playerHand;
        this.pullDemander = pullDemander;
        this.play = play;
        this.drawn = drawn;
        this.isCardCorrect = isCardCorrect;
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public PullDemander getPullRequest() {
        return pullDemander;
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
