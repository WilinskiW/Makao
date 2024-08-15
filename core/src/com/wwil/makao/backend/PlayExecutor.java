package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class PlayExecutor {
    private final MakaoBackend backend;

    public PlayExecutor(MakaoBackend backend) {
        this.backend = backend;
    }

    public PlayReport createPlayReport(Player player,Play play) {
        PlayReport playReport = new PlayReport(player, play);

        switch (play.getAction()){
            case END:
                return end(playReport);
            case PUT:
                putCard(play.getCardPlayed());
                return playReport.setCardCorrect(true);
            case PULL:
               return pull(playReport);
        }

        return playReport;
    }

    private PlayReport end(PlayReport playReport){
        //Sprawdź czy player nie został po zmieniony
        if(playReport.getPlayer() == currentPlayer()){
            backend.nextPlayer();
        }
        return playReport;
    }

    public void putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        if (currentPlayer() == humanPlayer()) {
            backend.humanPlayedCards.add(cardPlayed);
        }
        useCardAbility(cardPlayed);
    }

    private PlayReport pull(PlayReport playReport) {
        playReport.getPlayer().addCardToHand(playReport.getPlay().getDrawnCard());
        playReport.setSingleDrawn(playReport.getPlay().getDrawnCard());
        return playReport;
    }

    private void useCardAbility(Card card) {
        switch (card.getRank().getAbility()) {
            case CHANGE_SUIT:
                //useChangeSuitAbility();
                break;
            case PLUS_2:
                attack(2);
                break;
            case PLUS_3:
                attack(3);
                break;
            case WAIT:
                //useWaitAbility(wildCard);
                break;
            case DEMAND:
                //useDemandAbility(wildCard);
                break;
            case KING:
                chooseAbilityForKing(card);
                break;
            case WILD_CARD:
                //useWildCard();
        }
    }


    private void attack(int amountOfCards) {
        backend.getPlayMaker().increaseAmountOfPulls(amountOfCards);
        getNextPlayer().setAttack(true);
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(5);
                break;
            case SPADE:
                attackPrevious();
                attack(5);
                break;
        }
    }

    private void attackPrevious(){
        backend.getPlayMaker().increaseAmountOfPulls(5);
        backend.playerBefore();
        backend.currentPlayer().setAttack(true);
    }

    public Stack getStack() {
        return backend.getStack();
    }

    public Player humanPlayer() {
        return backend.getHumanPlayer();
    }

    public Player currentPlayer() {
        return backend.currentPlayer();
    }

    public Player getNextPlayer() {
        return backend.getNextPlayer();
    }
    public Player getPlayerBefore(){return backend.getPlayerBefore();}
}
