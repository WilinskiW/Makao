package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;
import com.wwil.makao.backend.states.BlockedState;
import com.wwil.makao.backend.states.PunishState;
import com.wwil.makao.backend.states.StateManager;

import java.util.ArrayList;
import java.util.List;

public class RoundManager {
    private final PlayerManager playerManager;
    private final DeckManager deckManager;
    private final StateManager stateManager;
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
        this.stateManager = new StateManager(this, playerManager);
        this.playExecutor = new PlayExecutor(this);
        startNewRound();
    }

    public boolean isCardValid(Card cardPlayed, boolean isChooserActive) {
        return stateManager.getHumanState().isValid(cardPlayed, validator);
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
            stateManager.setActionsActivation(true, false, true);
        } else {
            //Sprawdzenie czy gracz położył już wcześniej kartę.
            // Jeżeli pull jest aktywny to znaczy że jeszcze nie położył poprawnej karty
            if (stateManager.getHumanState().isPullActive()) {
                stateManager.setActionsActivation(true, true, false);
            }
        }
        return roundReport;
    }

    private RoundReport pullCard(Play humanPlay) {
        Human humanPlayer = playerManager.getHumanPlayer();
        humanPlay.setDrawnCard(deckManager.takeCardFromGameDeck());
        stateManager.handlePullAction(roundReport.whetherPlayerPulledRescue(humanPlayer));
        roundReport.addPlayRaport(playExecutor.createPlayReport(humanPlayer, humanPlay));
        return roundReport;
    }

    private RoundReport endTurn(Play humanPlay) {
        humanPlayedCards.clear();
        stateManager.setActionsActivation(false, false, false);
        roundReport.addPlayRaport(playExecutor.createPlayReport(playerManager.getHumanPlayer(), humanPlay));

        if (stateManager.isPlayerBlocked(playerManager.getHumanPlayer())) {
            PunishState blockedState = (BlockedState) stateManager.getHumanState();
            blockedState.decreaseAmount();
        }

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
        List<Play> plays = stateManager.generatePlays(player);
        for (Play play : plays) {
            roundReport.addPlayRaport(playExecutor.createPlayReport(player, play));
        }
    }

    RoundReport sendRoundReport() {
        RoundReport report = roundReport;
        stateManager.getHumanState().resetActionFlags();
        startNewRound();
        return report;
    }

    private void startNewRound() {
        roundReport = new RoundReport();
    }

    void increaseAmountOfPulls(int amount) {
        amountOfPulls += amount;
    }

    public int giveAmountOfPulls() {
        int amount = amountOfPulls;
        amountOfPulls = 0;
        return amount;
    }

    public CardValidator getValidator() {
        return validator;
    }

    void increaseAmountOfWaits() {
        amountOfWaits++;
    }

    public int giveAmountOfWaits() {
        int amount = amountOfWaits;
        amountOfWaits = 0;
        return amount;
    }

    public DeckManager getDeckManager() {
        return deckManager;
    }

    PlayerManager getPlayerManager() {
        return playerManager;
    }

    public int getAmountOfPulls() {
        return amountOfPulls;
    }

    StateManager getStateManager() {
        return stateManager;
    }

    RoundReport getRoundReport() {
        return roundReport;
    }

    List<Card> getHumanPlayedCards() {
        return humanPlayedCards;
    }
}
