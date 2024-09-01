package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.ComputerPlayMaker;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;

import java.util.List;

public class StateManager implements StateContext {
    private final ComputerPlayMaker computerPlayMaker;
    private final PlayerManager playerManager;
    private final StateChanger stateChanger;
    private final StateChecker stateChecker;
    private final HumanStateHandler humanStateHandler;

    public StateManager(RoundManager roundManager, PlayerManager playerManager) {
        this.computerPlayMaker = new ComputerPlayMaker(roundManager, this);
        this.playerManager = playerManager;
        this.stateChanger = new StateChanger(roundManager, this);
        this.stateChecker = new StateChecker();
        this.humanStateHandler = new HumanStateHandler(stateChanger, stateChecker, this, playerManager.getHumanPlayer());
    }

    public List<Play> generatePlays(Player currentPlayer) {
        return computerPlayMaker.generatePlays(currentPlayer);
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

    public StateChanger getStateChanger() {
        return stateChanger;
    }

    public HumanStateHandler getHumanStateHandler() {
        return humanStateHandler;
    }

    public StateChecker getStateChecker() {
        return stateChecker;
    }
}

