package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.*;

public class StateChanger {
    private final RoundManager roundManager;
    private final StateContext stateContext;

    public StateChanger(RoundManager roundManager, StateContext stateContext) {
        this.roundManager = roundManager;
        this.stateContext = stateContext;
    }

    public void applyDefaultState(Player player) {
        changePlayerState(player, new DefaultState());
    }

    public void applyDefenceState(Player player, Card attackingCard) {
        changePlayerState(player, new DefenseState(attackingCard));
    }

    public void transferDefenceState(Player currentPlayer, Card cardPlayed) {
        applyDefaultState(currentPlayer);
        Player targetPlayer = cardPlayed.isKingSpade()
                ? stateContext.getPreviousPlayer()
                : stateContext.getNextPlayer();
        applyDefenceState(targetPlayer, cardPlayed);
    }

    public void applyDefaultRescueState(Player player){
        changePlayerState(player, new DefaultRescueState());
    }

    protected void applyPunishment(Player player) {
        if (roundManager.getPullsCount() > 0) {
            applyPullingState(player);
        } else {
            applyBlockedState(player);
        }
    }

    public void applyPullingState(Player player) {
        changePlayerState(player, new PullingState(roundManager.giveAmountOfPulls() - 1));
        //-1, bo odejmujemy pociągnięcie rescue card
    }

    public void applyBlockedState(Player player) {
        changePlayerState(player, new BlockedState(roundManager.giveAmountOfWaits()));
    }

    private void changePlayerState(Player player, State newState) {
        stateContext.changeState(player, newState);
    }
}
