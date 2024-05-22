package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

//Raport karty, którą zagrał gracz lub dobrania
public class PlayReport {
    private final Player player;
    private final Play play;
    private Card drawn;
    private boolean isCardCorrect;
    private boolean blocked;
    private List<Card> cardsToPull;

    public PlayReport(Player player, Play play) {
        this.play = play;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public Play getPlay() {
        return play;
    }

    public Card getDrawn() {
        return drawn;
    }

    public PlayReport setDrawn(Card drawn) {
        this.drawn = drawn;
        return this;
    }

    public boolean isCardCorrect() {
        return isCardCorrect;
    }

    public PlayReport setCardCorrect(boolean cardCorrect) {
        isCardCorrect = cardCorrect;
        return this;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public PlayReport setBlocked(boolean blocked) {
        this.blocked = blocked;
        return this;
    }

    public List<Card> getCardsToPull() {
        return cardsToPull;
    }

    public PlayReport setCardsToPull(List<Card> cardsToPull) {
        this.cardsToPull = cardsToPull;
        return this;
    }
}
