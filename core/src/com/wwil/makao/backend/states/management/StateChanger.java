package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.gameplay.RoundManager;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.*;

public class StateChanger {
    private final RoundManager roundManager;
    private final StateContext stateContext;

    protected StateChanger(RoundManager roundManager, StateContext stateContext) {
        this.roundManager = roundManager;
        this.stateContext = stateContext;
    }

    public void applyDefaultState(Player player) {
        changePlayerState(player, new DefaultState());
    }

    public void applyDefenceState(Player player, Card attackingCard) {
        changePlayerState(player, new DefenseState(attackingCard));
    }

    public void applyDefaultRescueState(Player player) {
        changePlayerState(player, new DefaultRescueState());
    }

    public void applyDemandRescueState(Player player){
        changePlayerState(player, new DemandRescueState());
    }

    public void applyDefenceRescueState(Player player) {
        boolean isAttackedByFour = roundManager.getDeckManager().peekStackCard().getRank() == Rank.FOUR;
        changePlayerState(player, new DefenceRescueState(isAttackedByFour));
    }

    protected void applyPunishment(Player player) {
        if (roundManager.getPullsCount() > 0) {
            applyPullingState(player);
            handlePullingState(player);
        } else {
            applyBlockedState(player);
        }
    }

    public void handlePullingState(Player player) {
        PunishState pullingState = (PullingState) player.getState();
        pullingState.decreaseAmount();
        if (pullingState.getAmountOfPunishes() == 0) {
            applyDefaultState(player);
            roundManager.getCardsPlayedInTurn().clear();
        }
    }

    private void applyPullingState(Player player) {
        changePlayerState(player, new PullingState(roundManager.giveAmountOfPulls() - 1));
        //-1, bo odejmujemy pociągnięcie rescue card
    }

    public void applyChoosingDemandState(Player player) {
        changePlayerState(player, new ChoosingDemandState());
    }

    public void applyChoosingSuitState(Player player) {
        changePlayerState(player, new ChoosingSuitState());
    }

    public void applyAllDefenceState(Card attackedCard) {
        roundManager.getPlayerManager().getPlayers().forEach(player -> applyDefenceState(player, attackedCard));
    }

    private void applyBlockedState(Player player) {
        changePlayerState(player, new BlockedState(roundManager.giveAmountOfWaits()));
    }

    private void changePlayerState(Player player, State newState) {
        stateContext.changeState(player, newState);
    }
}
