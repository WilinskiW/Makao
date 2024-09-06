package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;

public class HumanPlayAnalyzer {
    private final RoundManager roundManager;
    private final Human humanPlayer;
    private final PlayExecutor playExecutor;

    HumanPlayAnalyzer(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.humanPlayer = roundManager.getPlayerManager().getHumanPlayer();
        this.playExecutor = roundManager.getPlayExecutor();
    }

    public boolean isCardValid(Card cardPlayed) {
        return humanPlayer.getState().isValid(cardPlayed, roundManager.getValidator());
    }

    public RoundReport processHumanPlay(Play humanPlay) {
        switch (humanPlay.getAction()) {
            case PUT:
                return putCard(humanPlay);
            case PULL:
                return pullCard(humanPlay);
            case END:
                return endTurn(humanPlay);
            default:
                throw new IllegalArgumentException("Unsupported action");
        }
    }

    private RoundReport putCard(Play humanPlay) {
        boolean isValid = isCardValid(humanPlay.getCardPlayed());
        PlayReport putPlayReport;

        if (isValid) {
            putPlayReport = playExecutor.createPlayReport(humanPlayer, humanPlay);
        } else {
            putPlayReport = new PlayReport(humanPlayer, humanPlay).setCardCorrect(false);
            putPlayReport.setState(humanPlayer.getState());
        }

        roundManager.getRoundReport().addPlayRaport(putPlayReport);

        return roundManager.getRoundReport();
    }

    private RoundReport pullCard(Play humanPlay) {
        humanPlay.setDrawnCard(roundManager.getDeckManager().takeCardFromGameDeck());

        PlayReport pullPlayReport = playExecutor.createPlayReport(humanPlayer, humanPlay);
        roundManager.getRoundReport().addPlayRaport(pullPlayReport);

        return roundManager.getRoundReport();
    }

    private RoundReport endTurn(Play humanPlay) {
        PlayReport endPlayReport = playExecutor.createPlayReport(humanPlayer, humanPlay);
        roundManager.getRoundReport().addPlayRaport(endPlayReport);

        return roundManager.playRound();
    }
}
