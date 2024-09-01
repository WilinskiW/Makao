package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.states.management.StateManager;

import java.util.ArrayList;
import java.util.List;

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

    public boolean isCardValid(Card cardPlayed, boolean isChooserActive) {
        return stateManager.getHumanState().isValid(cardPlayed, roundManager.getValidator());
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
        boolean isValid = isCardValid(humanPlay.getCardPlayed(), humanPlay.isChooserActive());
        stateManager.getHumanStateHandler().handleStateAfterPut(isValid, roundManager.getCardsPlayedInTurn().size());
        PlayReport putPlayReport = new PlayReport(humanPlayer, humanPlay).setCardCorrect(isValid);
        roundManager.getRoundReport().addPlayRaport(putPlayReport);

        if (isValid) {
            playExecutor.executePutPlay(putPlayReport);
        }


        return roundManager.getRoundReport();
    }

    private RoundReport pullCard(Play humanPlay) {
        humanPlay.setDrawnCard(roundManager.getDeckManager().takeCardFromGameDeck());
        stateManager.getHumanStateHandler().handleStateAfterPull(roundManager.getRoundReport().whetherPlayerPulledRescue(humanPlayer));
        roundManager.getRoundReport().addPlayRaport(playExecutor.createPlayReport(humanPlayer, humanPlay));
        return roundManager.getRoundReport();
    }

    private RoundReport endTurn(Play humanPlay) {
        roundManager.getCardsPlayedInTurn().clear();
        stateManager.getHumanStateHandler().handleStateAfterEnd();
        roundManager.getRoundReport().addPlayRaport(playExecutor.createPlayReport(humanPlayer, humanPlay));
        return roundManager.playRound();
    }
}
