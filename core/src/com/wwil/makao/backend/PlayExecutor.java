package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.List;

public class PlayExecutor {
    private final MakaoBackend backend;
    private final List<Card> pullDeck = new ArrayList<>();

    public PlayExecutor(MakaoBackend backend) {
        this.backend = backend;
    }

    public PlayReport createPlayReport(Play play) {
        PlayReport playReport = new PlayReport(currentPlayer(), play);

        //1. Jeżeli gracz jest atakowany to dobierz karty
        if(currentPlayer().isAttack()){
            multiPull(playReport);
            return playReport;
        }

        //2. Jeżeli masz kartę do zagrania to połóż ją
        if (play.getCardPlayed() != null) {
            putCard(play.getCardPlayed());
            return playReport.setCardCorrect(true);
        }

        //3. Jeżeli nie spełniłeś powyższych kroków to dobierz jedną kartę
        if (play.getAction() == Action.PULL) {
            singlePull(playReport);
        }

        return playReport;
    }

    private void multiPull(PlayReport playReport){
        List<Card> cardsToPull = new ArrayList<>(pullDeck);
        currentPlayer().addCardsToHand(cardsToPull);
        currentPlayer().setAttack(false);
        playReport.setCardsToPull(cardsToPull);
        pullDeck.clear();
    }

    private void singlePull(PlayReport playReport) {
        Card drawn = backend.takeCardFromGameDeck();
        playReport.getPlayer().addCardToHand(drawn);
        playReport.setSingleDrawn(drawn);
    }

    public void putCard(Card cardPlayed) {
        getStack().addCardToStack(cardPlayed);
        currentPlayer().removeCardFromHand(cardPlayed);
        if (currentPlayer() == humanPlayer()) {
            backend.humanPlayedCards.add(cardPlayed);
        }
        useCardAbility(cardPlayed);
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
        pullDeck.addAll(backend.giveCards(amountOfCards));
        getNextPlayer().setAttack(true);
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(5);
                break;
            case SPADE:
                //attackPrevious(5);
                attack(5);
                break;
        }
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
    public Player getNextPlayer(){return backend.getNextPlayer();}
}
