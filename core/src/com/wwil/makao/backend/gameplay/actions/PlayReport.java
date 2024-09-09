package com.wwil.makao.backend.gameplay.actions;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;

public class PlayReport {
    private final Player player;
    private final State beforeState;
    private State afterState;
    private final Play play;
    private Card drawn;
    private boolean isCardCorrect;

    public PlayReport(Player player, Play play) {
        this.play = play;
        this.beforeState = player.getState().copyState();
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

    public Card getDrawn() {
        return drawn;
    }

    public void setDrawn(Card drawn) {
        this.drawn = drawn;
    }

    public State getBeforeState() {
        return beforeState;
    }

    public State getAfterState() {
        return afterState;
    }

    public void setAfterState(State afterState) {
        this.afterState = afterState.copyState();
    }
}
