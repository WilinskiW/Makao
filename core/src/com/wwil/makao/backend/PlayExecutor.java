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
                return executePullPlay(playReport);
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

    private PlayReport executePutPlay(PlayReport playReport, Card cardPlayed) {
        putCard(cardPlayed);
        return playReport.setCardCorrect(true);
    }

    public void putCard(Card cardPlayed) {
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

    private PlayReport executePullPlay(PlayReport playReport) {
        playReport.getPlayer().addCardToHand(playReport.getPlay().getDrawnCard());
        playReport.setSingleDrawn(playReport.getPlay().getDrawnCard());
        return playReport;
    }
}
