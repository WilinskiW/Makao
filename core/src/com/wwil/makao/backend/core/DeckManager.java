package com.wwil.makao.backend.core;

import com.wwil.makao.backend.model.card.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DeckManager {
    private final LinkedList<Card> gameDeck;
    private final Stack stack;

    DeckManager() {
        this.gameDeck = createCardsToGameDeck();
        this.stack = new Stack();
        //todo: Zmienic po testach
        //stack.addCardToStack(getStartStackCard());
        stack.addCardToStack(new Card(Rank.FIVE, Suit.SPADE));
    }

    private LinkedList<Card> createCardsToGameDeck() {
        LinkedList<Card> cards = new CardFactory().createCards();
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

    public void addToStack(Card card) {
        stack.addCardToStack(card);
    }

    public Card peekStackCard() {
        return stack.peekCard();
    }

    public Card takeCardFromGameDeck() {
        if (gameDeck.isEmpty()) {
            refreshGameDeck();
        }
        return gameDeck.pollLast();
    }

    private void refreshGameDeck() {
        Card stackCard = stack.pollLast();
        gameDeck.addAll(getStackCards(stackCard));
        stack.getCards().clear();
        stack.addCardToStack(stackCard);
    }

    private List<Card> getStackCards(Card lastStackCard) {
        List<Card> cards = stack.getCards()
                .stream()
                .filter(card -> !card.isShadow())
                .collect(Collectors.toList());
        cards.remove(lastStackCard);
        return cards;
    }
}
