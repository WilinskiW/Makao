package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;

public class PlayExecutor {
    private final RoundManager roundManager;
    private final AbilityHandler abilityHandler;

    PlayExecutor(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.abilityHandler = new AbilityHandler(this.roundManager);
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
            roundManager.getPlayerManager().nextPlayer();
        }
        return playReport;
    }

    PlayReport executePutPlay(PlayReport playReport) {
        putCard(playReport);
        return playReport.setCardCorrect(true).setPutActive().setEndActive();
    }

    private void putCard(PlayReport playReport) {
        Card cardPlayed = playReport.getPlay().getCardPlayed();
        removeCardFromPlayerHand(cardPlayed);
        addToStack(cardPlayed);
        if (isHumanPlayer()) {
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

    private boolean isHumanPlayer() {
        return roundManager.getPlayerManager().getCurrentPlayer() == roundManager.getPlayerManager().getHumanPlayer();
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
        playReport.setPutActive().setEndActive();
        return playReport;
//        if (player.isAttack()) {
//            roundManager.decreaseAmountOfPulls();
//        }
    }

//    private PlayReport evaluateHumanAvailableActions(Player player, PlayReport playReport) {
//        if (!isHumanPlayer()) {
//            return playReport;
//        }
//
////        if (player.isAttack()) {
////            return evaluateActionsWhileAttack(player, playReport);
////        } else {
////            return evaluateActionsInNormalTurn(player, playReport);
////        }
//    }

    private PlayReport evaluateActionsWhileAttack(Player player, PlayReport playReport) {
        if (isFirstPull(player)) {
            return playReport.setPutActive().setPullActive();
        }

        if (hasRemainingPulls()) {
            return playReport.setPullActive();
        }

//        player.setAttack(false);
        return playReport.setEndActive();
    }

    private boolean isFirstPull(Player player) {
        return !roundManager.getRoundReport().hasPlayerPullBefore(player);
    }

    private boolean hasRemainingPulls() {
        return roundManager.getAmountOfPulls() > 0;
    }

    private PlayReport evaluateActionsInNormalTurn(Player player, PlayReport playReport) {
        if (isFirstPull(player)) {
            return playReport.setPutActive().setEndActive();
        }
        return playReport.setPutActive().setPullActive();
    }
}
