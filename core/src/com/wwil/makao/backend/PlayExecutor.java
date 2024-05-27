package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayExecutor {
    private final MakaoBackend backend;
    private final List<Card> pullDeck = new ArrayList<>();
    private final CardValidator validator;

    public PlayExecutor(MakaoBackend backend) {
        this.backend = backend;
        this.validator = backend.getValidator();
    }

    public PlayReport createReport(Play play) {
        PlayReport playReport = new PlayReport(currentPlayer(), play);
        //Połóż kartę/karty
        if (play.getCardsPlayed() != null) {
            putCards(play);
            return playReport.setCardCorrect(true);
        }

        //Dobierz karty
        if (!pullDeck.isEmpty()) {
            drainPullDeck(playReport);
            backend.setEvent(new DefaultEvent(backend));
        }

        //Dobierz kartę
        if (play.getAction() == Action.PULL) {
            pull(playReport);
        }


        return playReport;
    }

    private void drainPullDeck(PlayReport playReport) {
        List<Card> cardsToPull = new ArrayList<>(pullDeck);
        playReport.setCardsToPull(cardsToPull);
        currentPlayer().addCardsToHand(cardsToPull);
        pullDeck.clear();
    }

    public void pull(PlayReport playReport) {
        Card drawn = backend.takeCardFromGameDeck();
        currentPlayer().addCardToHand(drawn);
//        if (validator.isValidCardForCurrentEvent(drawn, backend.getEvent())) {
//            if (currentPlayer() != humanPlayer()) {
//                playReport.getPlay().setCardsPlayed(Collections.singletonList(drawn));
//                System.out.println("Pierwsza karta ratuje!");
//            }
//        }
        playReport.setDrawn(drawn);
    }


    private void putCards(Play play) {
        for (Card card : play.getCardsPlayed()) {
            putCard(card);
        }
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
                attackNext(2, card);
                break;
            case PLUS_3:
                attackNext(3, card);
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


    private void attackNext(int amountOfCards, Card attackingCard) {
        pullDeck.addAll(backend.giveCards(amountOfCards));
        backend.setEvent(new BattleNextEvent(backend, attackingCard));
    }

    private void attackPrevious(int amountOfCards, Card attackingCard) {
        pullDeck.addAll(backend.giveCards(amountOfCards));
        backend.setEvent(new BattlePreviousEvent(backend, attackingCard));
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attackNext(5, card);
                break;
            case SPADE:
                attackPrevious(5, card);
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
}
