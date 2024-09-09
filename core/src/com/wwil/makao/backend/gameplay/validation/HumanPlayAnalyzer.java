package com.wwil.makao.backend.gameplay.validation;

import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.backend.gameplay.management.PlayExecutor;
import com.wwil.makao.backend.gameplay.management.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;

public class HumanPlayAnalyzer {
    private final RoundManager roundManager;
    private final Human humanPlayer;
    private final PlayExecutor playExecutor;

    public HumanPlayAnalyzer(RoundManager roundManager) {
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
            case MAKAO:
                return informMakao(humanPlay);
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
            putPlayReport.setAfterState(humanPlayer.getState());
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

    private RoundReport informMakao(Play humanPlay) {
        PlayReport makaoReport = playExecutor.createPlayReport(humanPlayer, humanPlay);
        roundManager.getRoundReport().addPlayRaport(makaoReport);

        return roundManager.getRoundReport();
    }
}
