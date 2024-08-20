package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;

public class PlayReport {
    private final Player player;
    private final Play play;
    private Card drawn;
    private boolean isCardCorrect;
    private boolean isPutActive;
    private boolean isPullActive;
    private boolean isEndActive;
    private boolean isChooserActive;

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

    PlayReport setCardCorrect(boolean cardCorrect) {
        isCardCorrect = cardCorrect;
        return this;
    }

    public Card getDrawn() {
        return drawn;
    }

    void setDrawn(Card drawn) {
        this.drawn = drawn;
    }

    public boolean isPutActive() {
        return isPutActive;
    }

    PlayReport setPutActive() {
        isPutActive = true;
        return this;
    }

    public boolean isPullActive() {
        return isPullActive;
    }

    PlayReport setPullActive() {
        isPullActive = true;
        return this;
    }

    public boolean isEndActive() {
        return isEndActive;
    }

    protected PlayReport setEndActive() {
        isEndActive = true;
        return this;
    }

    public boolean isChooserActive() {
        return isChooserActive;
    }

    public void setChooserActive(boolean isChooserActive) {
        this.isChooserActive = isChooserActive;
    }
}
