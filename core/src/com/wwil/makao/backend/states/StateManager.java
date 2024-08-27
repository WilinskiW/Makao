package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.ComputerPlayMaker;
import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Human;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;

import java.util.List;

public class StateManager {
    private final RoundManager roundManager;
    private final ComputerPlayMaker computerPlayMaker;
    private final PlayerManager playerManager;

    public StateManager(RoundManager roundManager, PlayerManager playerManager) {
        this.roundManager = roundManager;
        this.computerPlayMaker = new ComputerPlayMaker(roundManager, this);
        this.playerManager = playerManager;
    }

    public List<Play> generatePlays(Player currentPlayer) {
        return computerPlayMaker.generatePlays(currentPlayer);
    }

    public void handleStateAfterPut(boolean isValid, int humanPlayedCards) {
        if (isValid) {
            setActionsActivation(true, false, true);
        } else if (humanPlayedCards == 0) {
            setActionsActivation(true, true, false);
        }
    }

    public void handleStateAfterPull(boolean hasPullBefore) {
        Human humanPlayer = playerManager.getHumanPlayer();
        if (isDefenseState(humanPlayer)) {
            if (hasPullBefore) {
                if (roundManager.getPullsCount() > 0) {
                    applyPullingState(humanPlayer);
                } else {
                    applyBlockedState(humanPlayer);
                }
            }
        } else {
            setActionsActivation(true, false, true);
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
            setActionsActivation(false, true, false);
        } else {
            applyDefaultState(humanPlayer);
            setActionsActivation(true, true, false);
        }
    }

    public void handleStateAfterEnd(Human humanPlayer) {
        if (isPlayerBlocked(humanPlayer)) {
            PunishState blockedState = (BlockedState) humanPlayer.getState();
            blockedState.decreaseAmount();
        }
        setActionsActivation(false, false, false);
    }

    public void setActionsActivation(boolean isPutActive, boolean isPullActive, boolean isEndActive) {
        getHumanState().setPutActive(isPutActive);
        getHumanState().setPullActive(isPullActive);
        getHumanState().setEndActive(isEndActive);
    }

    public PlayerState getHumanState() {
        return playerManager.getHumanPlayer().getState();
    }

    public void applyDefenceState(Player player, Card attackingCard) {
        changePlayerState(player, new DefenseState(player, attackingCard));
    }

    public void transferDefenceState(Player currentPlayer, Card cardPlayed) {
        applyDefaultState(currentPlayer);
        if (cardPlayed.isKingSpade()) {
            applyDefenceState(playerManager.getPreviousPlayer(), cardPlayed);
        } else {
            applyDefenceState(playerManager.getNextPlayer(), cardPlayed);
        }
    }

    public void applyDefaultState(Player player) {
        changePlayerState(player, new DefaultState(player));
    }

    public void applyPullingState(Player player) {
        changePlayerState(player, new PullingState(player, roundManager.giveAmountOfPulls() - 1));
        //-1, bo odejmujemy pociągnięcie rescue card
    }

    public void applyBlockedState(Player player) {
        changePlayerState(player, new BlockedState(player, roundManager.giveAmountOfWaits()));
    }

    private void changePlayerState(Player player, PlayerState newState) {
        player.changeState(newState);
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
}
