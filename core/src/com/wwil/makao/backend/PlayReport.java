package com.wwil.makao.backend;

//Raport karty, którą zagrał gracz lub dobrania
public class PlayReport {
    private PlayerHand player;
    private AbilityReport abilityReport;
    private final Play play;
    private Card drawn;
    private boolean isCardCorrect;
    private boolean blocked;

    public PlayReport(PlayerHand player,AbilityReport abilityReport, Play play, Card drawn, boolean isCardCorrect, boolean blocked) {
        this.player = player;
        this.abilityReport = abilityReport;
        this.play = play;
        this.drawn = drawn;
        this.isCardCorrect = isCardCorrect;
        this.blocked = blocked;
    }

    public PlayReport(PlayerHand player,Play play) {
        this.play = play;
        this.player = player;
    }

    public PlayerHand getPlayer() {
        return player;
    }

    public PlayReport setPlayer(PlayerHand player) {
        this.player = player;
        return this;
    }

    public AbilityReport getAbilityReport() {
        return abilityReport;
    }

    public PlayReport setAbilityReport(AbilityReport abilityReport) {
        this.abilityReport = abilityReport;
        return this;
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
}
