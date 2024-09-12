package com.wwil.makao.backend.gameplay.management;

import com.wwil.makao.backend.gameplay.actions.Play;
import com.wwil.makao.backend.gameplay.actions.PlayReport;
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
            case MAKAO:
                return handleMakaoAction(player, playReport);
            default:
                throw new IllegalArgumentException("Unsupported action: " + play.getAction());
        }
    }

    private PlayReport handlePutAction(PlayReport playReport) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        Player player = playReport.getPlayer();

        abilityHandler.useCardAbility(cardPlayed, roundManager.getDeckManager().peekStackCard());
        removeCardFromPlayerHand(player, cardPlayed);
        updateGameAfterPut(cardPlayed);
        updatePlayerState(player, playReport);
        return playReport.setCardCorrect(true);
    }

    private void removeCardFromPlayerHand(Player player, Card cardPlayed) {
        if (!cardPlayed.isShadow()) {
            player.removeCardFromHand(cardPlayed);
        }
    }

    private void updateGameAfterPut(Card cardPlayed) {
        roundManager.getDeckManager().addToStack(cardPlayed);
        if (!cardPlayed.matchesRank(Rank.JOKER)) {
            roundManager.getGameStateManager().getCardsPlayedInTurn().add(cardPlayed);
        }
    }

    private void updatePlayerState(Player player, PlayReport playReport) {
        stateHandler.updatePlayerState(player, playReport.getPlay());
        playReport.setAfterState(player.getState());
    }

    private PlayReport handleEndAction(PlayReport playReport) {
        roundManager.getGameStateManager().getCardsPlayedInTurn().clear();
        updatePlayerState(playReport.getPlayer(), playReport);
        roundManager.getPlayerManager().proceedToNextPlayerIfNeeded(playReport.getPlayer());
        return playReport;
    }

    private PlayReport handlePullAction(Player player, PlayReport playReport) {
        Card drawnCard = playReport.getPlay().getDrawnCard();
        addCardToPlayerHand(playReport, drawnCard);
        updatePlayerState(player, playReport);
        return playReport;
    }

    private void addCardToPlayerHand(PlayReport playReport, Card drawnCard) {
        playReport.getPlayer().addCardToHand(drawnCard);
        playReport.setDrawn(drawnCard);
    }

    private PlayReport handleMakaoAction(Player player, PlayReport playReport) {
        player.handleMakaoAction(roundManager.getPlayerManager().getHumanPlayer(), stateHandler);
        playReport.setAfterState(player.getState());
        return playReport;
    }
}
