package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.states.management.StateManager;

public class HumanPlayAnalyzer {
    private final RoundManager roundManager;
    private final Human humanPlayer;
    private final StateManager stateManager;
    private final PlayExecutor playExecutor;

    public HumanPlayAnalyzer(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.humanPlayer = roundManager.getPlayerManager().getHumanPlayer();
        this.stateManager = roundManager.getStateManager();
        this.playExecutor = roundManager.getPlayExecutor();
    }

    public boolean isCardValid(Card cardPlayed) {
        return stateManager.getPlayerState().isValid(cardPlayed, roundManager.getValidator());
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
            stateManager.getStateHandler().updateStateAfterPut(humanPlayer);
            putPlayReport = playExecutor.createPlayReport(humanPlayer, humanPlay);
        } else {
            putPlayReport = new PlayReport(humanPlayer, humanPlay).setCardCorrect(false);
        }

        roundManager.getRoundReport().addPlayRaport(putPlayReport);

        return roundManager.getRoundReport();
    }

    private RoundReport pullCard(Play humanPlay) {
        humanPlay.setDrawnCard(roundManager.getDeckManager().takeCardFromGameDeck());

        boolean rescued = roundManager.getRoundReport().whetherPlayerPulledRescue(humanPlayer);
        stateManager.getStateHandler().updateStateAfterPull(humanPlayer, rescued);

        PlayReport pullPlayReport = playExecutor.createPlayReport(humanPlayer, humanPlay);
        roundManager.getRoundReport().addPlayRaport(pullPlayReport);

        return roundManager.getRoundReport();
    }

    private RoundReport endTurn(Play humanPlay) {
        stateManager.getStateHandler().updateStateAfterEnd(humanPlayer);

        PlayReport endPlayReport = playExecutor.createPlayReport(humanPlayer, humanPlay);
        roundManager.getRoundReport().addPlayRaport(endPlayReport);

        return roundManager.playRound();
    }
}
