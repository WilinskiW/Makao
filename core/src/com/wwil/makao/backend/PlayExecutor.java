package com.wwil.makao.backend;

public class PlayExecutor {
    private final RoundManager roundManager;
    private final AbilityHandler abilityHandler;

    public PlayExecutor(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.abilityHandler = new AbilityHandler(this.roundManager.getPlayerManager(), this.roundManager.getPlayMaker());
    }

    public PlayReport createPlayReport(Player player, Play play) {
        PlayReport playReport = new PlayReport(player, play);

        switch (play.getAction()) {
            case END:
                return executeEndPlay(playReport);
            case PUT:
                return executePutPlay(playReport, play.getCardPlayed());
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

    protected PlayReport executePutPlay(PlayReport playReport, Card cardPlayed) {
        putCard(cardPlayed);
        return playReport.setCardCorrect(true).setPutActive().setEndActive();
    }

    private void putCard(Card cardPlayed) {
        addToStack(cardPlayed);
        removeCardFromPlayerHand(cardPlayed);
        if (isHumanPlayer()) {
            addCardToHumanPlayed(cardPlayed);
        }
        abilityHandler.useCardAbility(cardPlayed);
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
        pullCard(player, playReport);
        return evaluateHumanAvailableActions(player, playReport);
    }

    private void pullCard(Player player, PlayReport playReport) {
        Card drawnCard = playReport.getPlay().getDrawnCard();
        player.addCardToHand(drawnCard);
        playReport.setSingleDrawn(drawnCard);

        if (player.isAttack()) {
            decreaseAmountOfPulls();
        }
    }

    private void decreaseAmountOfPulls() {
        roundManager.setAmountOfPulls(roundManager.getAmountOfPulls() - 1);
    }

    private PlayReport evaluateHumanAvailableActions(Player player, PlayReport playReport) {
        if (!isHumanPlayer()) {
            return playReport;
        }

        if (player.isAttack()) {
            return evaluateActionsWhileAttack(player, playReport);
        } else {
            return evaluateActionsInNormalTurn(player, playReport);
        }
    }

    private PlayReport evaluateActionsWhileAttack(Player player, PlayReport playReport) {
        if (isFirstPull(player)) {
            return playReport.setPutActive().setPullActive();
        }

        if (hasRemainingPulls()) {
            return playReport.setPullActive();
        }

        player.setAttack(false);
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
