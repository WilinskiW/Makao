package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
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

    void useCardAbility(PlayReport playReport) {
        Card card = playReport.getPlay().getCardPlayed();

        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                changeSuit(playReport);
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
                demand(playReport);
                break;
            case KING:
                chooseAbilityForKing(card);
                break;
            case WILD_CARD:
                createCard(playReport);
                break;
        }
    }

    private void changeSuit(PlayReport playReport) {
        if (playReport.getPlayer() == playerManager.getHumanPlayer()) {
            if (!playReport.getPlay().isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }

    private void attackNext(int amountOfCards, Card card) {
        roundManager.increaseAmountOfPulls(amountOfCards);
        stateManager.getStateChanger().applyDefenceState(playerManager.getNextPlayer(), card);
    }

    private void blockNext(Card card){
        roundManager.increaseAmountOfWaits();
        stateManager.getStateChanger().applyDefenceState(playerManager.getNextPlayer(), card);
    }

    private void demand(PlayReport playReport) {
        if (playReport.getPlayer() == playerManager.getHumanPlayer()) {
            if (!playReport.isChooserActive()) {
                playReport.setChooserActive(true);
            }
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
        }
    }

    private void attackPrevious(Card card) {
        roundManager.increaseAmountOfPulls(5);
        stateManager.getStateChanger().applyDefenceState(playerManager.getPreviousPlayer(), card);
        playerManager.goToPreviousPlayer();
    }

    private void createCard(PlayReport playReport) {
        if (playReport.getPlayer() == playerManager.getHumanPlayer()) {
            if (!playReport.isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }
}
