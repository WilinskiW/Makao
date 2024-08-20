package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;

import java.util.ArrayList;
import java.util.List;

public class RoundManager {
    private final PlayerManager playerManager;
    private final DeckManager deckManager;
    private final PlayMaker playMaker;
    private final PlayExecutor playExecutor;
    private final CardValidator validator;
    private RoundReport roundReport;
    private int amountOfPulls = 0;
    private int amountOfWaits = 0;
    private final List<Card> humanPlayedCards = new ArrayList<>();

    public RoundManager(PlayerManager playerManager, DeckManager deckManager) {
        this.playerManager = playerManager;
        this.deckManager = deckManager;
        this.validator = new CardValidator(this, deckManager);
        this.playMaker = new PlayMaker(this);
        this.playExecutor = new PlayExecutor(this);
        startNewRound();
    }

    public boolean isCardValid(Card cardPlayed, boolean isChooserActive) {
        return playerManager.getHumanPlayer().getState().isValid(cardPlayed,validator);
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
        PlayReport putPlayReport = new PlayReport(playerManager.getHumanPlayer(), humanPlay).setCardCorrect(isValid);
        roundReport.addPlayRaport(putPlayReport);

        if (isValid) {
            playExecutor.executePutPlay(putPlayReport);
        } else {
            putPlayReport.setPutActive().setPullActive();
        }
        return roundReport;
    }

    private RoundReport pullCard(Play humanPlay) {
        humanPlay.setDrawnCard(deckManager.takeCardFromGameDeck());
        roundReport.addPlayRaport(playExecutor.createPlayReport(playerManager.getCurrentPlayer(), humanPlay));
        return roundReport;
    }

    private RoundReport endTurn(Play humanPlay) {
        humanPlayedCards.clear();
        roundReport.addPlayRaport(playExecutor.createPlayReport(playerManager.getHumanPlayer(), humanPlay));
        playRound();
        return sendRoundReport();
    }

    void playRound() {
        while (isComputerTurn() || hasSomeoneWon()) {
            executePlays(playerManager.getCurrentPlayer());
        }
    }

    private boolean isComputerTurn() {
        return playerManager.getCurrentPlayer() != playerManager.getHumanPlayer();
    }

    private boolean hasSomeoneWon() {
        return playerManager.getCurrentPlayer().checkIfPlayerHaveNoCards();
    }

    private void executePlays(Player player) {
        List<Play> plays = playMaker.makePlays(player);
        for (Play play : plays) {
            roundReport.addPlayRaport(playExecutor.createPlayReport(player, play));
        }
    }

    RoundReport sendRoundReport() {
        RoundReport report = roundReport;
        startNewRound();
        return report;
    }

    private void startNewRound() {
        roundReport = new RoundReport();
    }

    public void increaseAmountOfPulls(int amount) {
        amountOfPulls += amount;
    }

    public void decreaseAmountOfPulls() {
        amountOfPulls--;
    }

    void increaseAmountOfWaits() {
        amountOfWaits++;
    }

    void decreaseAmountOfWaits() {
        amountOfWaits--;
    }

    public CardValidator getValidator() {
        return validator;
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

    RoundReport getRoundReport() {
        return roundReport;
    }

    PlayerManager getPlayerManager() {
        return playerManager;
    }

    public int getAmountOfPulls() {
        return amountOfPulls;
    }

    public void setAmountOfPulls(int amountOfPulls) {
        this.amountOfPulls = amountOfPulls;
    }

    public int getAmountOfWaits() {
        return amountOfWaits;
    }

    public void setAmountOfWaits(int amountOfWaits) {
        this.amountOfWaits = amountOfWaits;
    }

    List<Card> getHumanPlayedCards() {
        return humanPlayedCards;
    }
}
