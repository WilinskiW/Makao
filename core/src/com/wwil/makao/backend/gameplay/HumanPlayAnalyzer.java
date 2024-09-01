package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.states.StateManager;

import java.util.ArrayList;
import java.util.List;

public class HumanPlayAnalyzer {
    private final RoundManager roundManager;
    private final Human humanPlayer;
    private final StateManager stateManager;
    private final PlayExecutor playExecutor;
    private final List<Card> humanPlayedCards = new ArrayList<>();

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
        PlayReport putPlayReport = new PlayReport(humanPlayer, humanPlay).setCardCorrect(isValid);
        roundManager.getRoundReport().addPlayRaport(putPlayReport);

        if (isValid) {
            playExecutor.executePutPlay(putPlayReport);
        }

        stateManager.getHumanStateHandler().handleStateAfterPut(isValid, humanPlayedCards.size());

        return roundManager.getRoundReport();
    }

    private RoundReport pullCard(Play humanPlay) {
        humanPlay.setDrawnCard(roundManager.getDeckManager().takeCardFromGameDeck());
        stateManager.getHumanStateHandler().handleStateAfterPull(roundManager.getRoundReport().whetherPlayerPulledRescue(humanPlayer));
        roundManager.getRoundReport().addPlayRaport(playExecutor.createPlayReport(humanPlayer, humanPlay));
        return roundManager.getRoundReport();
    }

    private RoundReport endTurn(Play humanPlay) {
        humanPlayedCards.clear();
        roundManager.getRoundReport().addPlayRaport(playExecutor.createPlayReport(humanPlayer, humanPlay));
        stateManager.getHumanStateHandler().handleStateAfterEnd(humanPlayer);
        return roundManager.playRound();
    }

    public List<Card> getHumanPlayedCards() {
        return humanPlayedCards;
    }
}
