package com.wwil.makao.backend;

//Raport karty, którą zagrał gracz lub dobrania
public class PlayReport {
    private final PlayerHand playerHand;
    private final AbilityReport abilityReport;
    private final Play play;
    private final Card drawn;
    private final boolean isCardCorrect;

    public PlayReport(PlayerHand playerHand, AbilityReport abilityReport, Play play, Card drawn, boolean isCardCorrect) {
        this.playerHand = playerHand;
        this.abilityReport = abilityReport;
        this.play = play;
        this.drawn = drawn;
        this.isCardCorrect = isCardCorrect;
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public AbilityReport getAbilityReport() {
        return abilityReport;
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

}
