package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//Tworzy z play'ów report
public class PlayExecutor {
    private final MakaoBackend backend;
    private final List<Card> pullDeck = new ArrayList<>();
    private final CardValidator validator;

    public PlayExecutor(MakaoBackend backend) {
        this.backend = backend;
        this.validator = backend.getValidator();
    }

    public PlayReport executePlay(Play play) {
        PlayReport playReport = new PlayReport(currentPlayer(), play);
        //Dobierz kartę
        if (currentPlayer().isAttack()) {
            drainPullDeck(playReport);
        } else if (play.getAction() == Action.PULL) {
            pull(playReport);
        }

        //Połóż kartę/karty
        if (play.getCardsPlayed() != null) {
            putCards(play);
        }
        return playReport.setCardCorrect(true);
    }

    private void drainPullDeck(PlayReport playReport) {
        List<Card> cardsToPull = new ArrayList<>(pullDeck);
        playReport.setCardsToPull(cardsToPull);
        currentPlayer().addCardsToHand(cardsToPull);
        pullDeck.clear();
        currentPlayer().setAttacker(null);
    }

    public void pull(PlayReport playReport) {
        Card drawn = backend.takeCardFromGameDeck();
        currentPlayer().addCardToHand(drawn);
        if (validator.isValidCardForCurrentState(drawn)) {
            if (currentPlayer() != humanPlayer()) {
                playReport.getPlay().setCardsPlayed(Collections.singletonList(drawn));
                System.out.println("Pierwsza karta ratuje!");
            }
        }
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
                attack(getNextPlayer(), 2, card);
                break;
            case PLUS_3:
                attack(getNextPlayer(), 3, card); //ATAK
                break;
            case WAIT:
                //useWaitAbility(wildCard);
                break;
            case DEMAND:
                //useDemandAbility(wildCard); //ATAK
                break;
            case KING:
                chooseAbilityForKing(card); //ATAK
                break;
            case WILD_CARD:
                //useWildCard(); //PRAWDOPODOBNY ATAK
        }
    }


    private void attack(Player player, int amountOfCards, Card attackingCard) {
        pullDeck.addAll(backend.giveCards(amountOfCards));
        player.setAttacker(new CardBattle(pullDeck, attackingCard));
        if (attackingCard.isBattleCard()) {
        }
    }

    private void chooseAbilityForKing(Card card) {
        switch (card.getSuit()) {
            case HEART:
                attack(getNextPlayer(), 5, card);
                break;
            case SPADE:
                attack(getPlayerBefore(), 5, card);
                break;
        }
    }

    public Stack getStack(){
        return backend.getStack();
    }

    public Player humanPlayer(){
        return backend.getHumanPlayer();
    }

    public Player currentPlayer(){
        return backend.getCurrentPlayer();
    }

    public Player getNextPlayer(){
        return backend.getNextPlayer();
    }

    public Player getPlayerBefore(){
        return backend.getPlayerBefore();
    }
}
