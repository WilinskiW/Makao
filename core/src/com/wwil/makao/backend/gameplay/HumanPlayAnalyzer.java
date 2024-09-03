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
        //Sprawdzenia czy karta jest położona (TYLKO DLA CZŁOWIEKA, Komputer wybiera zawsze poprawne)
        boolean isValid = isCardValid(humanPlay.getCardPlayed()); //C
        stateManager.getStateHandler().updateStateAfterPut(humanPlayer,isValid, roundManager.getCardsPlayedInTurn().size()); //C K
        PlayReport putPlayReport = new PlayReport(humanPlayer, humanPlay).setCardCorrect(isValid); // C K
        roundManager.getRoundReport().addPlayRaport(putPlayReport); //C K

        if (isValid) { // C
            playExecutor.executePutPlay(putPlayReport); //
        }

        return roundManager.getRoundReport();
    }

    private RoundReport pullCard(Play humanPlay) {
        humanPlay.setDrawnCard(roundManager.getDeckManager().takeCardFromGameDeck()); //C K
        stateManager.getStateHandler().updateStateAfterPull(humanPlayer,roundManager.getRoundReport().whetherPlayerPulledRescue(humanPlayer)); // C K
        roundManager.getRoundReport().addPlayRaport(playExecutor.createPlayReport(humanPlayer, humanPlay));// C K
        return roundManager.getRoundReport();
    }

    private RoundReport endTurn(Play humanPlay) {
        stateManager.getStateHandler().updateStateAfterEnd(humanPlayer); // C K
        roundManager.getRoundReport().addPlayRaport(playExecutor.createPlayReport(humanPlayer, humanPlay)); // C K
        return roundManager.playRound(); // C
    }
}
