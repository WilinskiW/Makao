package com.wwil.makao.backend.states.management;

import com.wwil.makao.backend.gameplay.management.RoundManager;
import com.wwil.makao.backend.model.card.Ability;
import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.player.Player;
import com.wwil.makao.backend.states.State;
import com.wwil.makao.backend.states.impl.choosing.ChoosingCardState;
import com.wwil.makao.backend.states.impl.choosing.ChoosingDemandState;
import com.wwil.makao.backend.states.impl.choosing.ChoosingSuitState;
import com.wwil.makao.backend.states.impl.DefenseState;
import com.wwil.makao.backend.states.impl.NormalState;
import com.wwil.makao.backend.states.impl.punish.BlockedState;
import com.wwil.makao.backend.states.impl.punish.MakaoPunishState;
import com.wwil.makao.backend.states.impl.punish.PullingState;
import com.wwil.makao.backend.states.impl.base.PunishState;
import com.wwil.makao.backend.states.impl.rescue.DefenceRescueState;
import com.wwil.makao.backend.states.impl.rescue.DemandRescueState;
import com.wwil.makao.backend.states.impl.rescue.NormalRescueState;

public class StateChanger {
    private final RoundManager roundManager;
    private final StateContext stateContext;

    protected StateChanger(RoundManager roundManager, StateContext stateContext) {
        this.roundManager = roundManager;
        this.stateContext = stateContext;
    }

    public void applyNormalState(Player player) {
        changePlayerState(player, new NormalState());
    }

    public void applyDefenceState(Player player, Card attackingCard) {
        changePlayerState(player, new DefenseState(attackingCard));
    }

    public void applyRescueState(Player player) {
        DefenseState defenseState = (DefenseState) player.getState();
        if (defenseState.getAttackingCard().getRank().getAbility() == Ability.NONE) {
            applyDemandRescueState(player);
        } else {
            applyDefenceRescueState(player);
        }
    }

    public void applyNormalRescueState(Player player) {
        changePlayerState(player, new NormalRescueState());
    }

    private void applyDemandRescueState(Player player) {
        changePlayerState(player, new DemandRescueState());
    }

    private void applyDefenceRescueState(Player player) {
        boolean isAttackedByFour = roundManager.getDeckManager().peekStackCard().getRank() == Rank.FOUR;
        changePlayerState(player, new DefenceRescueState(isAttackedByFour));
    }

    public void applyPunishment(Player player) {
        PunishState punishState = null;
        if (roundManager.getGameStateManager().getPullsCount() > 0) {
            int pulls = roundManager.getGameStateManager().giveAmountOfPulls() - 1; // -1, bo odejmujemy pociągnięcie rescue card
            punishState = new PullingState(pulls);
        } else if (roundManager.getGameStateManager().getWaitsCount() > 0) {
            int waits = roundManager.getGameStateManager().giveAmountOfWaits();
            punishState = new BlockedState(waits);
        }

        if (punishState != null) {
            changePlayerState(player, punishState);
            handlePunishState(player, punishState);
        }
    }

    public void handlePunishState(Player player, PunishState punish) {
        punish.decreaseAmount();
        if (punish.getAmountOfPunishes() <= 0) {
            applyNormalState(player);
            roundManager.getGameStateManager().getCardsPlayedInTurn().clear();
        }
    }

    public void applyMakaoPunishState(Player player) {
        changePlayerState(player, new MakaoPunishState());
    }

    public void applyChoosingDemandState(Player player) {
        changePlayerState(player, new ChoosingDemandState());
    }

    public void applyChoosingSuitState(Player player) {
        changePlayerState(player, new ChoosingSuitState());
    }

    public void applyChoosingCardState(Player player) {
        changePlayerState(player, new ChoosingCardState(player.getState()));
    }

    public boolean deactivateChoosing(Card card) {
        return (card.isShadow() && !roundManager.getDeckManager().isStackCardBeforeLastIsJoker());
    }

    public void applyAllDefenceState(Card attackedCard) {
        roundManager.getPlayerManager().getPlayers().forEach(player -> applyDefenceState(player, attackedCard));
    }

    public void setActions(Player player, boolean put, boolean pull, boolean end, boolean makao) {
        stateContext.activateActions(player, put, pull, end, makao);
    }

    private void changePlayerState(Player player, State newState) {
        stateContext.changeState(player, newState);
    }
}
