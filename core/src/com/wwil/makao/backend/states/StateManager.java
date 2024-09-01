package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.ComputerPlayMaker;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;

import java.util.List;

public class StateManager implements StateContext {
    private final ComputerPlayMaker computerPlayMaker;
    private final PlayerManager playerManager;
    private final StateChanger stateChanger;

    public StateManager(RoundManager roundManager, PlayerManager playerManager) {
        this.computerPlayMaker = new ComputerPlayMaker(roundManager, this);
        this.playerManager = playerManager;
        this.stateChanger = new StateChanger(roundManager, this);
    }

    public List<Play> generatePlays(Player currentPlayer) {
        return computerPlayMaker.generatePlays(currentPlayer);
    }

    public void handleStateAfterPut(boolean isValid, int humanPlayedCards) {
        if (isValid) {
            activateActions(true, false, true);
        } else if (humanPlayedCards == 0) {
            activateActions(true, true, false);
        }
    }

    public void handleStateAfterPull(boolean hasPullBefore) {
        Human humanPlayer = playerManager.getHumanPlayer();
        if (isDefenseState(humanPlayer)) {
            if (hasPullBefore) {
                stateChanger.applyPunishment(humanPlayer);
            }
        } else {
            activateActions(true, false, true);
        }

        if (isPullingState(humanPlayer)) {
            handlePullingState();
        }
    }

    private void handlePullingState() {
        Human humanPlayer = playerManager.getHumanPlayer();
        PunishState pullingState = (PullingState) getHumanState();
        pullingState.decreaseAmount();
        if (pullingState.getAmountOfPunishes() > 0) {
            activateActions(false, true, false);
        } else {
            stateChanger.applyDefaultState(humanPlayer);
            activateActions(true, true, false);
        }
    }

    public void handleStateAfterEnd(Human humanPlayer) {
        if (isPlayerBlocked(humanPlayer)) {
            PunishState blockedState = (BlockedState) humanPlayer.getState();
            blockedState.decreaseAmount();
        }
        activateActions(false, false, false);
    }

    public void activateActions(boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        getHumanState().setPutActive(isPutActive);
        getHumanState().setPullActive(isPullActive);
        getHumanState().setEndActive(isEndActive);
    }

    @Override
    public void changeState(Player player, State newState) {
        player.changeState(newState);
    }

    @Override
    public Player getPreviousPlayer() {
        return playerManager.getPreviousPlayer();
    }

    @Override
    public State getHumanState() {
        return playerManager.getHumanPlayer().getState();
    }

    @Override
    public Player getNextPlayer() {
        return playerManager.getNextPlayer();
    }

    public boolean isPlayerBlocked(Player player) {
        return player.getState() instanceof BlockedState;
    }

    public boolean isPullingState(Player player) {
        return player.getState() instanceof PullingState;
    }

    public boolean isDefenseState(Player player) {
        return player.getState() instanceof DefenseState;
    }

    public StateChanger getStateChanger() {
        return stateChanger;
    }
}

