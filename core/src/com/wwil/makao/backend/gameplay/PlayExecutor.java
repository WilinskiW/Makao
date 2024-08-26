package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.PlayerState;

public class PlayExecutor {
    private final RoundManager roundManager;
    private final AbilityHandler abilityHandler;

    PlayExecutor(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.abilityHandler = new AbilityHandler(this.roundManager, roundManager.getStateManager());
    }

    PlayReport createPlayReport(Player player, Play play) {
        PlayReport playReport = new PlayReport(player, play);

        switch (play.getAction()) {
            case END:
                return executeEndPlay(playReport);
            case PUT:
                return executePutPlay(playReport);
            case PULL:
                return executePullPlay(player, playReport);
            default:
                throw new IllegalArgumentException("Unsupported action");
        }
    }

    private PlayReport executeEndPlay(PlayReport playReport) {
        if (roundManager.getPlayerManager().shouldProceedToNextPlayer(playReport.getPlayer())) {
            roundManager.getPlayerManager().goToNextPlayer();
        }
        return playReport;
    }

    PlayReport executePutPlay(PlayReport playReport) {
        putCard(playReport);
        return playReport.setCardCorrect(true);
    }

    private void putCard(PlayReport playReport) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        removeCardFromPlayerHand(cardPlayed);
        addToStack(cardPlayed);

        if (playReport.getPlayer().isHuman()) {
            addCardToHumanPlayed(cardPlayed);
        }

        abilityHandler.useCardAbility(playReport);
    }

    private void addToStack(Card cardPlayed) {
        roundManager.getDeckManager().getStack().addCardToStack(cardPlayed);
    }

    private void removeCardFromPlayerHand(Card cardPlayed) {
        roundManager.getPlayerManager().getCurrentPlayer().removeCardFromHand(cardPlayed);
    }

    private void addCardToHumanPlayed(Card cardPlayed) {
        roundManager.getHumanPlayedCards().add(cardPlayed);
    }

    private PlayReport executePullPlay(Player player, PlayReport playReport) {
        return pullCard(player, playReport);
    }

    private PlayReport pullCard(Player player, PlayReport playReport) {
        Card drawnCard = playReport.getPlay().getDrawnCard();
        player.addCardToHand(drawnCard);
        playReport.setDrawn(drawnCard);
        return playReport;
    }
}
