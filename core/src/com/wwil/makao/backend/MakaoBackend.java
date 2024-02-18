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
                useTwoAbility(currentPlayerIndex);
                break;
            case THREE:
                useThreeAbility(currentPlayerIndex);
                break;
            default:
                System.out.println("Nie two i three");
        }
    }

    private void useTwoAbility(int playerIndex) {
        int lastIndex = players.size() - 1;
        if (playerIndex != lastIndex) {
            players.get(playerIndex+1).addCardsToHand(giveCards(2));
        } else {
            players.get(0).addCardsToHand(giveCards(2));
        }
    }

    private void useThreeAbility(int playerIndex){
        int lastIndex = players.size() - 1;
        if (playerIndex != lastIndex) {
            players.get(playerIndex+1).addCardsToHand(giveCards(3));
        } else {
            players.get(0).addCardsToHand(giveCards(3));
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