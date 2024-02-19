package com.wwil.makao.backend;

import com.wwil.makao.backend.cardComponents.Card;
import com.wwil.makao.backend.cardComponents.CardFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakaoBackend {
    private final int STARTING_CARDS = 5;
    private final int AMOUNT_OF_PLAYERS = 4;
    public static List<Card> gameDeck;
    private final Stack stack = new Stack();
    private final List<PlayerHand> players = new ArrayList<>();
    private boolean inputBlock = false;


    //Konstruktor tworzy karty i graczy.
    public MakaoBackend() {
        createCardsToGameDeck();
        stack.addCardToStack(takeCardFromGameDeck());
        createPlayers();
    }

    private void createCardsToGameDeck() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        gameDeck = cards;
    }


    private void createPlayers() {
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            PlayerHand playerHand = new PlayerHand(giveCards(STARTING_CARDS));
            players.add(playerHand);
        }
    }

    public Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }

    private List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    public void playerPullCard(int playerIndex){
        players.get(playerIndex).addCardToHand(takeCardFromGameDeck());
    }

    public void useCardAbility(Card card, int currentPlayerIndex) {
        switch (card.getRank()) {
            case TWO:
                usePlusNextPlayerAbility(currentPlayerIndex,2);
                break;
            case THREE:
                usePlusNextPlayerAbility(currentPlayerIndex,3);
                break;
            case FOUR:
                useFourAbility(currentPlayerIndex);
            case K:
                useKingAbility(card,currentPlayerIndex);
                break;
        }
    }

    private void usePlusNextPlayerAbility(int playerIndex, int amountOfCards){
        int lastIndex = players.size() - 1;
        if (playerIndex != lastIndex) {
            players.get(playerIndex+1).addCardsToHand(giveCards(amountOfCards));
        } else {
            players.get(0).addCardsToHand(giveCards(amountOfCards));
        }
    }

    private void useFourAbility(int playerIndex){
        int lastIndex = players.size() - 1;
        if (lastIndex != playerIndex) {
            players.get(playerIndex + 1).setWaiting(true);
        } else {
            players.get(0).setWaiting(true);
        }
    }

    public void useKingAbility(Card card, int currentPlayerIndex){
        switch (card.getSuit()){
            case HEART:
                usePlusNextPlayerAbility(currentPlayerIndex,5);
                break;
            case SPADE:
                usePlusPreviousPlayerAbility(currentPlayerIndex);
        }
    }

    private void usePlusPreviousPlayerAbility(int playerIndex){
        int lastIndex = players.size() - 1;
        if (playerIndex != 0) {
            players.get(playerIndex-1).addCardsToHand(giveCards(5));
        } else {
            players.get(lastIndex).addCardsToHand(giveCards(5));
        }
    }
    //TODO: JOKER
    public boolean isCorrectCard(Card chosenCard) {
        Card stackCard = peekCardFromStack();
        if (stackCard.getRank().name().equals("Q") || chosenCard.getRank().name().equals("Q")
            /*|| chosenCard.getRank().name().equals("JOKER")*/) {
            return true;
        }

        return compareCards(chosenCard, stackCard);
    }

    private Card peekCardFromStack() {
        List<Card> stackCards = stack.getCards();
        int lastIndexOfStack = stackCards.size() - 1;
        return stack.getCards().get(lastIndexOfStack);
    }

    private boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    public Stack getStack() {
        return stack;
    }

    public List<PlayerHand> getPlayers() {
        return players;
    }

    public boolean isInputBlock() {
        return inputBlock;
    }

    public void setInputBlock(boolean inputBlock) {
        this.inputBlock = inputBlock;
    }
}

//Sama logika