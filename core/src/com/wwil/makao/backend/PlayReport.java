package com.wwil.makao.backend;

public class PlayReport {
    private final Player player;
    private final Play play;
    private Card singleDrawn;
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

    public Card getSingleDrawn() {
        return singleDrawn;
    }

    public void setSingleDrawn(Card singleDrawn) {
        this.singleDrawn = singleDrawn;
    }
}
