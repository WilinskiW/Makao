package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakaoBackend {
    private final int STARTING_CARDS = 5;
    private final int AMOUNT_OF_PLAYERS = 4;
    public static List<Card> gameDeck;
    private Stack stack = new Stack();
    private final List<PlayerHand> players = new ArrayList<>();
    private boolean inputBlock = false;


    //Konstruktor tworzy karty i graczy.
    public MakaoBackend() {
        createCardsToGameDeck();
        createPlayers();
    }

    private void createCardsToGameDeck(){
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        gameDeck = cards;
    }

    private void createPlayers() {
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            PlayerHand playerHand = new PlayerHand(giveStartingCards());
            players.add(playerHand);
        }
    }


    private List<Card> giveStartingCards() {
        List<Card> startingCards = new ArrayList<>();
        for (int i = 0; i < STARTING_CARDS; i++) {
            startingCards.add(getCard());
        }
        return startingCards;
    }


    public Card getCardFromGameDeck() {
        return getCard();
    }

    private List<Card> giveCards(int amount){
        List<Card> cards = new ArrayList<>();
        for(int i = 0; i < amount; i++){
            cards.add(getCardFromGameDeck());
        }
        return cards;
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

    private Card peekCardFromStack(){
        List<Card> stackCards = stack.getCards();
        int lastIndexOfStack = stackCards.size()-1;
        return stack.getCards().get(lastIndexOfStack);
    }

    private boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }

    public Stack getStack() {
        return stack;
    }

    public Card getCard() {
        return gameDeck.remove(0);
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