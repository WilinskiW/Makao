package com.wwil.makao.backend.core;

import com.wwil.makao.backend.model.card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckManager {
    private final List<Card> gameDeck;
    private final Stack stack = new Stack();

    DeckManager() {
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

    public List<Card> giveCards(int amount) {
        List<Card> cards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            cards.add(takeCardFromGameDeck());
        }
        return cards;
    }

    public Card peekStackCard() {
        return stack.peekCard();
    }

    public Card peekStackCardBeforeLast() {
        return stack.peekDemandCard();
    }
    public boolean isRankEqualsStackCardRank(Rank rank){
        return rank == peekStackCard().getRank();
    }

    public Card takeCardFromGameDeck() {
        return gameDeck.remove(0);
    }
    public boolean isRefreshNeeded(){
        return stack.getCards().size() > 3;
    }

    public List<Card> getGameDeck() {
        return gameDeck;
    }

    public Stack getStack() {
        return stack;
    }

}
