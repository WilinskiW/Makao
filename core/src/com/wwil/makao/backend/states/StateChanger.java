package com.wwil.makao.backend.states;

import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.model.player.PlayerManager;

public class StateChanger {
    private final RoundManager roundManager;
    private final PlayerManager playerManager;

    public StateChanger(RoundManager roundManager, PlayerManager playerManager) {
        this.roundManager = roundManager;
        this.playerManager = playerManager;
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
    protected void applyPunishment(Player player) {
        if (roundManager.getPullsCount() > 0) {
            applyPullingState(player);
        } else {
            applyBlockedState(player);
        }
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
}
