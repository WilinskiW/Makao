package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakaoBackend {
    public List<Card> cards;
    private int STARTING_CARDS = 5;
    private int AMOUNT_OF_PLAYERS = 4;
    private List<PlayerHand> players = new ArrayList<>();
    public MakaoBackend() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        this.cards = cards;
        createPlayers();
    }

    private void createPlayers(){
        for(int i = 0; i < AMOUNT_OF_PLAYERS; i++){
            PlayerHand playerHand = new PlayerHand(giveStartingCards());
            players.add(playerHand);
        }
    }


    private List<Card> giveStartingCards(){
        List<Card> startingCards = new ArrayList<>();
        for(int i = 0; i < STARTING_CARDS; i++){
            startingCards.add(getCard()); //todo może się skończyć
        }
        return startingCards;
    }

    public boolean isCorrectCard(Card stackCard, Card chosenCard) {

        if (stackCard.getRank().name().equals("Q") || chosenCard.getRank().name().equals("Q") || chosenCard.getRank().name().equals("JOKER")) {
            return true;
        }

        return compareCards(chosenCard, stackCard);
    }

    private boolean compareCards(Card card1, Card card2) {
        return card1.getSuit() == card2.getSuit() || card1.getRank() == card2.getRank();
    }


    public Card getCard(){
        return cards.remove(0);
    }

    public List<Card> getCards() {
        return cards;
    }

    public List<PlayerHand> getPlayers() {
        return players;
    }
}

//Sama logika