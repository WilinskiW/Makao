package com.wwil.makao.backend.gameplay.management;

import com.wwil.makao.backend.core.DeckManager;
import com.wwil.makao.backend.gameplay.actions.RoundReport;
import com.wwil.makao.backend.gameplay.validation.CardValidator;
import com.wwil.makao.backend.gameplay.ai.ComputerTurnManager;
import com.wwil.makao.backend.gameplay.validation.HumanPlayAnalyzer;
import com.wwil.makao.backend.states.management.PlayerStateManager;

public class RoundManager {
    private final PlayerManager playerManager;
    private final DeckManager deckManager;
    private final PlayExecutor playExecutor;
    private final CardValidator validator;
    private final HumanPlayAnalyzer humanPlayAnalyzer;
    private final ComputerTurnManager computerTurnManager;
    private final GameStateManager gameStateManager;
    private final PlayerStateManager stateManager;
    private RoundReport roundReport;

    public RoundManager(PlayerManager playerManager, DeckManager deckManager) {
        this.playerManager = playerManager;
        this.deckManager = deckManager;
        this.stateManager = new PlayerStateManager(this, playerManager);
        this.gameStateManager = new GameStateManager();
        this.validator = new CardValidator(gameStateManager, deckManager);
        this.playExecutor = new PlayExecutor(this);
        startNewRound();
        this.humanPlayAnalyzer = new HumanPlayAnalyzer(this);
        this.computerTurnManager = new ComputerTurnManager(this);
    }

    public RoundReport playRound() {
        while (isComputerTurn()) {
            computerTurnManager.manageComputerTurn();
        }
        return sendRoundReport();
    }

    private boolean isComputerTurn() {
        return !playerManager.getCurrentPlayer().isHuman();
    }

    RoundReport sendRoundReport() {
        RoundReport report = roundReport;
        stateManager.resetAllActionsActivation();
        startNewRound();
        return report;
    }

    private void startNewRound() {
        this.roundReport = new RoundReport();
    }
    public DeckManager getDeckManager() {
        return deckManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public HumanPlayAnalyzer getHumanPlayAnalyzer() {
        return humanPlayAnalyzer;
    }

    public RoundReport getRoundReport() {
        return roundReport;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }
    PlayerStateManager getStateManager() {
        return stateManager;
    }

    public PlayExecutor getPlayExecutor() {
        return playExecutor;
    }

    public CardValidator getValidator() {
        return validator;
    }
}
