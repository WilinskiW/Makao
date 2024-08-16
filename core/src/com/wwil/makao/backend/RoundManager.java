package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class RoundManager {
    private final PlayerManager playerManager;
    private final DeckManager deckManager;
    private final PlayMaker playMaker;
    private final PlayExecutor playExecutor;
    private final CardValidator validator;
    private RoundReport roundReport;
    private final List<Card> humanPlayedCards = new ArrayList<>();

    public RoundManager(PlayerManager playerManager, DeckManager deckManager) {
        this.playerManager = playerManager;
        this.deckManager = deckManager;
        this.validator = new CardValidator(this, deckManager);
        this.playMaker = new PlayMaker(this);
        this.playExecutor = new PlayExecutor(this);
        startNewRound();
    }

    protected RoundReport processHumanPlay(Play humanPlay) {
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
        boolean isValid = validator.isValid(humanPlay.getCardPlayed());
        roundReport.addPlayRaport(new PlayReport(playerManager.getHumanPlayer(), humanPlay).setCardCorrect(isValid));

        if (isValid) {
            playExecutor.putCard(humanPlay.getCardPlayed());
        }
        return roundReport;
    }

    private RoundReport pullCard(Play humanPlay) {
        humanPlay.setDrawnCard(deckManager.takeCardFromGameDeck());
        roundReport.addPlayRaport(playExecutor.createPlayReport(playerManager.getCurrentPlayer(),humanPlay));
        return roundReport;
    }

    private RoundReport endTurn(Play humanPlay) {
        humanPlayedCards.clear();
        roundReport.addPlayRaport(playExecutor.createPlayReport(playerManager.getHumanPlayer(), humanPlay));
        playRound();
        return sendRoundReport();
    }

    private void playRound() {
        while (isComputerTurn() || hasSomeoneWon()) {
            executePlays(playerManager.getCurrentPlayer());
        }
    }

    private boolean isComputerTurn(){
        return playerManager.getCurrentPlayer() != playerManager.getHumanPlayer();
    }

    private boolean hasSomeoneWon(){
        return playerManager.getCurrentPlayer().checkIfPlayerHaveNoCards();
    }

    private void executePlays(Player player) {
        List<Play> plays = playMaker.makePlays(player);
        for (Play play : plays) {
            roundReport.addPlayRaport(playExecutor.createPlayReport(player, play));
        }
    }

    private RoundReport sendRoundReport() {
        RoundReport report = roundReport;
        startNewRound();
        return report;
    }

    private void startNewRound() {
        roundReport = new RoundReport();
    }

    protected CardValidator getValidator() {
        return validator;
    }

    protected DeckManager getDeckManager() {
        return deckManager;
    }

    protected PlayMaker getPlayMaker() {
        return playMaker;
    }

    protected PlayerManager getPlayerManager() {
        return playerManager;
    }

    public List<Card> getHumanPlayedCards() {
        return humanPlayedCards;
    }
}
