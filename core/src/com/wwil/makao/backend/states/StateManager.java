package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.Play;
import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;

import java.util.List;

public class StateManager {
    private final RoundManager roundManager;
    private final ComputerPlayFactory computerPlayFactory;
    private final PlayerManager playerManager;

    public StateManager(RoundManager roundManager, PlayerManager playerManager) {
        this.roundManager = roundManager;
        this.computerPlayFactory = new ComputerPlayFactory(roundManager, this);
        this.playerManager = playerManager;
    }

    public List<Play> generatePlays(Player currentPlayer) {
        return computerPlayFactory.generatePlays(currentPlayer, roundManager);
    }

    public void deactivateAllActions(PlayerState state) {
        state.setPutActive(false);
        state.setPullActive(false);
        state.setEndActive(false);
    }

    public PlayerState getHumanState() {
        return playerManager.getHumanPlayer().getState();
    }

    public void applyDefenceState(Player player, Card attackingCard) {
        changePlayerState(player, new DefenseState(player, attackingCard));
    }

    void transferDefenceState(Player currentPlayer, Card cardPlayed) {
        applyDefaultState(currentPlayer);
        if (cardPlayed.isKingSpade()) {
            applyDefenceState(playerManager.getPreviousPlayer(), cardPlayed);
        } else {
            applyDefenceState(playerManager.getNextPlayer(), cardPlayed);
        }
    }

    void applyDefaultState(Player player) {
        changePlayerState(player, new DefaultState(player));
    }

    void applyPullingState(Player player) {
        changePlayerState(player, new PullingState(player, roundManager.giveAmountOfPulls() - 1));
        //-1, bo odejmujemy pociągnięcie rescue card
    }

    void applyBlockedState(Player player) {
        changePlayerState(player, new BlockedState(player, roundManager.giveAmountOfWaits()));
    }

    private void changePlayerState(Player player, PlayerState newState) {
        player.changeState(newState);
    }

    boolean isPlayerBlocked(Player player) {
        return player.getState() instanceof BlockedState;
    }

    boolean isDefenseState(Player player) {
        return player.getState() instanceof DefenseState;
    }
}
