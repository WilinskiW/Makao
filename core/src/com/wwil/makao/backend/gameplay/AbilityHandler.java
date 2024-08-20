package com.wwil.makao.backend.gameplay;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.player.PlayerManager;

public class AbilityHandler {
    private final RoundManager roundManager;
    private final PlayerManager playerManager;

    AbilityHandler(RoundManager roundManager) {
        this.roundManager = roundManager;
        this.playerManager = roundManager.getPlayerManager();
    }

    void useCardAbility(PlayReport playReport) {
        Card card = playReport.getPlay().getCardPlayed();

        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                changeSuit(playReport);
                break;
            case PLUS_2:
                attack(2);
                break;
            case PLUS_3:
                attack(3);
                break;
            case WAIT:
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

    private void changeSuit(PlayReport playReport){
        if(playReport.getPlayer() == playerManager.getHumanPlayer()){
            if (!playReport.getPlay().isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }

    private void attack(int amountOfCards) {
        roundManager.increaseAmountOfPulls(amountOfCards);
        playerManager.getNextPlayer().setAttack(true);
    }

    private void demand(PlayReport playReport){
        if(playReport.getPlayer() == playerManager.getHumanPlayer()){
            if (!playReport.isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(5);
                break;
            case SPADE:
                attackPrevious();
                break;
        }
    }

    private void attackPrevious(){
        roundManager.increaseAmountOfPulls(5);
        playerManager.playerBefore();
        playerManager.getCurrentPlayer().setAttack(true);
    }

    private void createCard(PlayReport playReport){
        if(playReport.getPlayer() == playerManager.getHumanPlayer()){
            if (!playReport.isChooserActive()) {
                playReport.setChooserActive(true);
            }
        }
    }
}
