package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public class PlayReport {
    private final Player player;
    private final State state;
    private final Play play;
    private Card drawn;
    private boolean isCardCorrect;
    private boolean isChooserActive;

    public PlayReport(Player player, Play play) {
        this.play = play;
        this.player = player;
        this.state = player.getState();
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

    public boolean isChooserActive() {
        return isChooserActive;
    }

    public State getPlayerState() {
        return state;
    }

    public void setChooserActive(boolean isChooserActive) {
        this.isChooserActive = isChooserActive;
    }
}
