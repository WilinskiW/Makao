package com.wwil.makao.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckManager {
    private final List<Card> gameDeck;
    private final Stack stack = new Stack();
    public DeckManager(){
        this.gameDeck = createCardsToGameDeck();
        //todo: Zmienic po testach
        //stack.addCardToStack(getStartStackCard());
        stack.addCardToStack(new Card(Rank.FIVE, Suit.SPADE));
    }

    private List<Card> createCardsToGameDeck() {
        List<Card> cards = new CardFactory().createCards();
        Collections.shuffle(cards);
        return cards;
    }

    private Card getStartStackCard() {
        for (Card card : gameDeck) {
            if (card.getRank().getAbility().equals(Ability.NONE)) {
                return card;
            }
        }
        return null;
    }

    protected List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }
    public Card peekStackCard(){return stack.peekCard();}
    public Card peekStackCardBeforeLast(){
        return stack.peekCardBeforeLast();
    }
    protected Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }

    public List<Card> getGameDeck() {
        return gameDeck;
    }
    public Stack getStack() {
        return stack;
    }

}
