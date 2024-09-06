package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.management.StateHandler;

public class PlayExecutor {
    private final RoundManager roundManager;
    private final AbilityHandler abilityHandler;
    private final StateHandler stateHandler;

    PlayExecutor(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.abilityHandler = new AbilityHandler(this.roundManager, roundManager.getStateManager());
        this.stateHandler = roundManager.getStateManager().getStateHandler();
    }

    PlayReport createPlayReport(Player player, Play play) {
        PlayReport playReport = new PlayReport(player, play);
        switch (play.getAction()) {
            case PUT:
                return executePutPlay(playReport);
            case END:
                return executeEndPlay(playReport);
            case PULL:
                return executePullPlay(player, playReport);
            default:
                throw new IllegalArgumentException("Unsupported action");
        }
    }

    private PlayReport executePutPlay(PlayReport playReport) {
        putCard(playReport);
        return playReport.setCardCorrect(true);
    }

    private void putCard(PlayReport playReport) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        abilityHandler.useCardAbility(playReport);
        if (!cardPlayed.isShadow()) {
            removeCardFromPlayerHand(cardPlayed);
        }
        addToStack(cardPlayed);
        addCardToPlayedCards(cardPlayed);
        stateHandler.updateStateAfterPut(playReport.getPlayer(), playReport.getPlay().getCardPlayed());
        playReport.setAfterState(playReport.getPlayer().getState());
    }

    private void addToStack(Card cardPlayed) {
        roundManager.getDeckManager().addToStack(cardPlayed);
    }

    private void removeCardFromPlayerHand(Card cardPlayed) {
        roundManager.getPlayerManager().getCurrentPlayer().removeCardFromHand(cardPlayed);
    }

    private void addCardToPlayedCards(Card cardPlayed) {
        roundManager.getCardsPlayedInTurn().add(cardPlayed);
    }

    private PlayReport executeEndPlay(PlayReport playReport) {
        roundManager.getCardsPlayedInTurn().clear();
        stateHandler.updateStateAfterEnd(playReport.getPlayer());
        playReport.setAfterState(playReport.getPlayer().getState());

        if (roundManager.getPlayerManager().shouldProceedToNextPlayer(playReport.getPlayer())) {
            roundManager.getPlayerManager().goToNextPlayer();
        }
        return playReport;
    }

    private PlayReport executePullPlay(Player player, PlayReport playReport) {
        Card drawnCard = playReport.getPlay().getDrawnCard();
        player.addCardToHand(drawnCard);
        playReport.setDrawn(drawnCard);

        stateHandler.updateStateAfterPull(player);
        playReport.setAfterState(player.getState());

        return playReport;
    }
}
