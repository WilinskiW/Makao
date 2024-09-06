package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;
import com.wwil.makao.backend.states.management.StateManager;

import java.util.ArrayList;
import java.util.List;

public class RoundManager {
    private final PlayerManager playerManager;
    private final DeckManager deckManager;
    private final StateManager stateManager;
    private final PlayExecutor playExecutor;
    private final CardValidator validator;
    private final HumanPlayAnalyzer humanPlayAnalyzer;
    private final PlayMaker playMaker;
    private RoundReport roundReport;
    private final List<Card> cardsPlayedInTurn;
    private int pullsCount = 0;
    private int waitsCount = 0;

    public RoundManager(PlayerManager playerManager, DeckManager deckManager) {
        this.playerManager = playerManager;
        this.deckManager = deckManager;
        this.validator = new CardValidator(this, deckManager);
        this.stateManager = new StateManager(this, playerManager);
        this.playExecutor = new PlayExecutor(this);
        this.humanPlayAnalyzer = new HumanPlayAnalyzer(this);
        this.playMaker = new PlayMaker(this);
        this.cardsPlayedInTurn = new ArrayList<>();
        startNewRound();
    }

    RoundReport playRound() {
        while (isComputerTurn() || hasSomeoneWon()) {
            executePlays(playerManager.getCurrentPlayer());
        }
        return sendRoundReport();
    }

    private boolean isComputerTurn() {
        return playerManager.getCurrentPlayer() != playerManager.getHumanPlayer();
    }

    private boolean hasSomeoneWon() {
        return playerManager.getCurrentPlayer().checkIfPlayerHaveNoCards();
    }

    private void executePlays(Player player) {
        Play play = playMaker.generatePlay(player);
        roundReport.addPlayRaport(playExecutor.createPlayReport(player, play));
    }

    RoundReport sendRoundReport() {
        RoundReport report = roundReport;
        stateManager.resetAllActionsActivation();
        startNewRound();
        return report;
    }

    private void startNewRound() {
        roundReport = new RoundReport();
    }

    void increaseAmountOfPulls(int amount) {
        pullsCount += amount;
    }

    public int giveAmountOfPulls() {
        int amount = pullsCount;
        clearAmountOfPulls();
        return amount;
    }

    public void clearAmountOfPulls() {
        pullsCount = 0;
    }

    void increaseAmountOfWaits() {
        waitsCount++;
    }
    public int giveAmountOfWaits() {
        int amount = waitsCount;
        waitsCount = 0;
        return amount;
    }

    CardValidator getValidator() {
        return validator;
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public int getPullsCount() {
        return pullsCount;
    }

    public List<Card> getCardsPlayedInTurn() {
        return cardsPlayedInTurn;
    }

    public HumanPlayAnalyzer getHumanPlayAnalyzer() {
        return humanPlayAnalyzer;
    }

    StateManager getStateManager() {
        return stateManager;
    }

    RoundReport getRoundReport() {
        return roundReport;
    }

    PlayExecutor getPlayExecutor() {
        return playExecutor;
    }
}
