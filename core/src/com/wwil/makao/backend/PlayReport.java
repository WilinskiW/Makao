package com.wwil.makao.backend;

import java.util.List;
public class PlayReport {
    private final Player player;
    private final Play play;
    private List<Card> cardsToPull;
    private boolean isCardCorrect;

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

    public boolean isCardCorrect() {
        return isCardCorrect;
    }

    public PlayReport setCardCorrect(boolean cardCorrect) {
        isCardCorrect = cardCorrect;
        return this;
    }
    public List<Card> getCardsToPull() {
        return cardsToPull;
    }

    public void setCardsToPull(List<Card> cardsToPull) {
        this.cardsToPull = cardsToPull;
    }
}
