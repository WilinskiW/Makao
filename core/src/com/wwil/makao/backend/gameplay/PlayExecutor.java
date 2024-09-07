package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
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

    public PlayReport createPlayReport(Player player, Play play) {
        PlayReport playReport = new PlayReport(player, play);
        switch (play.getAction()) {
            case PUT:
                return handlePutAction(playReport);
            case END:
                return handleEndAction(playReport);
            case PULL:
                return handlePullAction(player, playReport);
            default:
                throw new IllegalArgumentException("Unsupported action: " + play.getAction());
        }
    }

    private PlayReport handlePutAction(PlayReport playReport) {
        putCard(playReport);
        return playReport.setCardCorrect(true);
    }

    private void putCard(PlayReport playReport) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        Player player = playReport.getPlayer();

        abilityHandler.useCardAbility(cardPlayed);
        removeCardFromPlayerHand(player, cardPlayed);
        updateGameAfterPut(cardPlayed);
        updatePlayerStateAfterPut(player, cardPlayed, playReport);
    }

    private void removeCardFromPlayerHand(Player player, Card cardPlayed) {
        if (!cardPlayed.isShadow()) {
            player.removeCardFromHand(cardPlayed);
        }
    }

    private void updateGameAfterPut(Card cardPlayed) {
        roundManager.getDeckManager().addToStack(cardPlayed);
        if (!cardPlayed.matchesRank(Rank.JOKER)) {
            roundManager.getCardsPlayedInTurn().add(cardPlayed);
        }
    }

    private void updatePlayerStateAfterPut(Player player, Card cardPlayed, PlayReport playReport) {
        stateHandler.updateStateAfterPut(player, cardPlayed);
        playReport.setState(player.getState());
    }

    private PlayReport handleEndAction(PlayReport playReport) {
        roundManager.getCardsPlayedInTurn().clear();
        updatePlayerStateAfterEnd(playReport);
        proceedToNextPlayerIfNeeded(playReport.getPlayer());
        return playReport;
    }

    private void updatePlayerStateAfterEnd(PlayReport playReport) {
        stateHandler.updateStateAfterEnd(playReport.getPlayer());
        playReport.setState(playReport.getPlayer().getState());
    }

    private void proceedToNextPlayerIfNeeded(Player currentPlayer) {
        if (roundManager.getPlayerManager().shouldProceedToNextPlayer(currentPlayer)) {
            roundManager.getPlayerManager().goToNextPlayer();
        }
    }

    private PlayReport handlePullAction(Player player, PlayReport playReport) {
        Card drawnCard = playReport.getPlay().getDrawnCard();
        addCardToPlayerHand(playReport, drawnCard);
        updatePlayerStateAfterPull(player, playReport);
        return playReport;
    }

    private void addCardToPlayerHand(PlayReport playReport, Card drawnCard) {
        playReport.getPlayer().addCardToHand(drawnCard);
        playReport.setDrawn(drawnCard);
    }

    private void updatePlayerStateAfterPull(Player player, PlayReport playReport) {
        stateHandler.updateStateAfterPull(player);
        playReport.setState(player.getState());
    }
}
