package com.wwil.makao.backend.gameplay.management;

import com.wwil.makao.backend.model.card.Card;
import com.wwil.makao.backend.model.card.Rank;
import com.wwil.makao.backend.states.management.PlayerStateManager;

public class AbilityHandler {
    private final GameStateManager gameStateManager;
    private final PlayerManager playerManager;
    private final PlayerStateManager playerStateManager;

    AbilityHandler(RoundManager roundManager, PlayerStateManager playerStateManager) {
        this.gameStateManager = roundManager.getGameStateManager();
        this.playerManager = roundManager.getPlayerManager();
        this.playerStateManager = playerStateManager;
    }

    void useCardAbility(Card chosenCard, Card stackCard) {
        switch (chosenCard.getRank().getAbility()) {
            case CHANGE_SUIT:
                changeSuit(chosenCard, stackCard);
                break;
            case PLUS_2:
                attackNext(2, chosenCard);
                break;
            case PLUS_3:
                attackNext(3, chosenCard);
                break;
            case WAIT:
                blockNext(chosenCard);
                break;
            case DEMAND:
                demand(chosenCard, stackCard);
                break;
            case KING:
                chooseAbilityForKing(chosenCard, stackCard);
                break;
            case WILD_CARD:
                chooseCard();
                break;
        }
    }

    private void changeSuit(Card card, Card stackCard) {
        if (!card.isShadow() || isStackCardJoker(stackCard)) {
            playerStateManager.getStateChanger().applyChoosingSuitState(playerManager.getCurrentPlayer());
        }
    }

    private void attackNext(int amountOfCards, Card card) {
        gameStateManager.increaseAmountOfPulls(amountOfCards);
        playerStateManager.getStateChanger().applyDefenceState(playerManager.getNextPlayer(), card);
    }

    private void blockNext(Card card) {
        gameStateManager.increaseAmountOfWaits();
        playerStateManager.getStateChanger().applyDefenceState(playerManager.getNextPlayer(), card);
    }

    private void demand(Card card, Card stackCard) {
        if (!card.isShadow() || isStackCardJoker(stackCard)) {
            playerStateManager.getStateChanger().applyChoosingDemandState(playerManager.getCurrentPlayer());
        }
    }

    private void chooseAbilityForKing(Card card, Card stackCard) {
        switch (card.getSuit()) {
            case HEART:
                attackNext(5, card);
                break;
            case SPADE:
                attackPrevious(card);
                break;
            case DIAMOND:
            case CLUB:
                neutralizePullsFromAttackingKing(stackCard);
                break;
        }
    }

    private void attackPrevious(Card card) {
        gameStateManager.increaseAmountOfPulls(5);
        playerStateManager.getStateChanger().applyDefenceState(playerManager.getPreviousPlayer(), card);
        playerManager.goToPreviousPlayer();
    }

    private void neutralizePullsFromAttackingKing(Card stackCard) {
        if (gameStateManager.getPullsCount() > 0 && isStackCardKing(stackCard)) {
            gameStateManager.clearAmountOfPulls();
        }
    }

    private boolean isStackCardJoker(Card stackCard){
        return stackCard.matchesRank(Rank.JOKER);
    }

    private boolean isStackCardKing(Card stackCard) {
        return stackCard.matchesRank(Rank.K);
    }

    private void chooseCard(){
        playerStateManager.getStateChanger().applyChoosingCardState(playerManager.getCurrentPlayer());
    }
}
