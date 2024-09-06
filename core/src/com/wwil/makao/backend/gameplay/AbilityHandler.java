package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.model.player.PlayerManager;
import com.wwil.makao.backend.states.management.StateManager;

public class AbilityHandler {
    private final RoundManager roundManager;
    private final PlayerManager playerManager;
    private final StateManager stateManager;

    AbilityHandler(RoundManager roundManager, StateManager stateManager) {
        this.roundManager = roundManager;
        this.playerManager = roundManager.getPlayerManager();
        this.stateManager = stateManager;
    }

    void useCardAbility(Card card) {
        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                changeSuit(card);
                break;
            case PLUS_2:
                attackNext(2, card);
                break;
            case PLUS_3:
                attackNext(3, card);
                break;
            case WAIT:
                blockNext(card);
                break;
            case DEMAND:
                demand(card);
                break;
            case KING:
                chooseAbilityForKing(card);
                break;
            case WILD_CARD:
                //todo
                break;
        }
    }

    private void changeSuit(Card card) {
        if (!card.isShadow()) {
            stateManager.getStateChanger().applyChoosingSuitState(playerManager.getCurrentPlayer());
        }
    }

    private void attackNext(int amountOfCards, Card card) {
        roundManager.increaseAmountOfPulls(amountOfCards);
        stateManager.getStateChanger().applyDefenceState(playerManager.getNextPlayer(), card);
    }

    private void blockNext(Card card) {
        roundManager.increaseAmountOfWaits();
        stateManager.getStateChanger().applyDefenceState(playerManager.getNextPlayer(), card);
    }

    private void demand(Card card) {
        if (!card.isShadow()) {
            stateManager.getStateChanger().applyChoosingDemandState(playerManager.getCurrentPlayer());
        }
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attackNext(5, card);
                break;
            case SPADE:
                attackPrevious(card);
                break;
            case DIAMOND:
            case CLUB:
                neutralizePullsFromAttackingKing();
                break;
        }
    }

    private void attackPrevious(Card card) {
        roundManager.increaseAmountOfPulls(5);
        stateManager.getStateChanger().applyDefenceState(playerManager.getPreviousPlayer(), card);
        playerManager.goToPreviousPlayer();
    }

    private void neutralizePullsFromAttackingKing() {
        if (roundManager.getPullsCount() > 0 && isStackCardKing()) {
            roundManager.clearAmountOfPulls();
        }
    }

    private boolean isStackCardKing() {
        return roundManager.getDeckManager().peekStackCard().matchesRank(Rank.K);
    }
}
